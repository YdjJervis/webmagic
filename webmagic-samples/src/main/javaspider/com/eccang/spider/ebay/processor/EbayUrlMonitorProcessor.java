package com.eccang.spider.ebay.processor;

import com.eccang.spider.base.monitor.ScheduledTask;
import com.eccang.spider.base.util.UrlUtils;
import com.eccang.spider.ebay.pojo.EbayUrl;
import com.eccang.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/17 9:47
 */
@Service
public class EbayUrlMonitorProcessor extends EbayProcessor implements ScheduledTask {

    @Override
    public void execute() {
        sLogger.info("开始执行 解析品类url 爬取任务...");
        List<EbayUrl> urlList = mEbayUrlService.findCategoryUrl(30);
        startToCrawl(urlList);
    }

    @Override
    protected void dealOtherPage(Page page) {
        EbayUrl url = getUrl(page);
        if (url.type == 0) {
            /*解析品类URL，并添加数据库*/
            extractChildCategoryUrl(page);

            /*解析搜索到的产品数*/
            int productsCount = extractProductsCount(page);

            /*计算页数,并将除第一页外的所有URL添加到数据库中*/
            if (productsCount != 0) {
                int index = productsCount % 200 > 0 ? (productsCount / 200) + 1 : productsCount / 200;
                if (index > 1) {
                    extractProductsListing(page, index);
                }

                /*解析第个品类下第一页的产品url，并存入数据库中*/
                extractProductsUrl(page);
            }
        } else {
            /*解析产品url，并存入数据库中*/
            extractProductsUrl(page);
        }
    }

    /**
     * 解析品类URL，并添加数据库
     */
    private void extractChildCategoryUrl(Page page) {

        List<Selectable> selectables = page.getHtml().xpath("//*[@id='LeftNavCategoryContainer']//div[@class='rlp-b']//div[@class='cat-link']/span[@class='cnt']").nodes();

        if (CollectionUtils.isNotEmpty(selectables)) {
            int index = selectables.size() > 1 ? 1 : 0;
            String str = selectables.get(index).xpath("/span/text()").get();
            String productNum = RegexUtil.reg(str, "(\\d+[,]?\\d+)");
            if (StringUtils.isEmpty(productNum) || productNum.equalsIgnoreCase("0")) {
                sLogger.info("品类（" + getUrl(page).categoryName + ")下没有子品类存在；url:" + getUrl(page).url);
                return;
            }
        }

        List<Selectable> nodes = page.getHtml().xpath("//*[@id='LeftNavCategoryContainer']//div[@class='rlp-b']//div[@class='cat-link ']/a").nodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            List<EbayUrl> urls = new ArrayList<>();
            EbayUrl url;
            for (Selectable node : nodes) {
                url = new EbayUrl();
                url.url = node.xpath("/a/@href").get();
                url.urlMD5 = UrlUtils.md5(url.url);
                if (mEbayUrlService.isExsit(url.urlMD5)) {
                    sLogger.info("============= url(" + url.url + ") has existed.");
                    continue;
                }
                url.categoryName = node.xpath("/a/text()").get();
                url.siteCode = getUrl(page).siteCode;
                url.type = 0;
                urls.add(url);
            }
            if (CollectionUtils.isNotEmpty(urls)) {
                mEbayUrlService.addAll(urls);
            }
        }
    }

    /**
     * 解析搜索到的产品数
     */
    private int extractProductsCount(Page page) {
        String productsCounts = page.getHtml().xpath("//*[@id='bciw']/div/span[@class='listingscnt']/text()").get();
        int productsCount = 0;
        if (StringUtils.isNotEmpty(productsCounts)) {
            productsCounts = productsCounts.replace("listings", "");
            if (productsCounts.contains(",")) {
                productsCounts = productsCounts.replace(",", "");
            }
            productsCount = Integer.valueOf(productsCounts.trim());
        }
        return productsCount;
    }

    /**
     * 解析产品listing页，除第一页外，并入库
     *
     * @param index 页数
     */
    private void extractProductsListing(Page page, int index) {
        index = index > 49 ? 49 : index;
        List<EbayUrl> urls = new ArrayList<>();
        EbayUrl url;
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='Pagination']//td[@class='pages']/a").nodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            String pageUrl = nodes.get(1).xpath("/a/@href").get();
            for (int i = 1; i < index; i++) {
                url = new EbayUrl();
                url.url = UrlUtils.setValue(pageUrl, "_pgn", String.valueOf(i));
                url.urlMD5 = UrlUtils.md5(url.url);
                url.categoryName = getUrl(page).categoryName;
                url.siteCode = getUrl(page).siteCode;
                url.type = 1;
                url.toString();
                urls.add(url);
            }
            if (CollectionUtils.isNotEmpty(urls)) {
                mEbayUrlService.addAll(urls);
            }
        }
    }

    /**
     * 解析产品url，并存入数据库中
     */
    private void extractProductsUrl(Page page) {
        List<Selectable> selectables = page.getHtml().xpath("//*[@id='ListViewInner']/li/h3[@class='lvtitle']/a").nodes();
        if (CollectionUtils.isNotEmpty(selectables)) {
            List<EbayUrl> urls = new ArrayList<>();
            EbayUrl ebayUrl;
            for (Selectable selectable : selectables) {
                ebayUrl = new EbayUrl();
                ebayUrl.url = selectable.xpath("/a/@href").get();
                ebayUrl.urlMD5 = UrlUtils.md5(ebayUrl.url);
                if (mEbayUrlService.isExsit(ebayUrl.urlMD5)) {
                    sLogger.info("============= urlMD5(" + ebayUrl.urlMD5 + "),url(" + ebayUrl.url + ") has existed.");
                    continue;
                }
                ebayUrl.type = 2;
                ebayUrl.categoryName = getUrl(page).categoryName;
                ebayUrl.siteCode = getUrl(page).siteCode;
                ebayUrl.toString();
                urls.add(ebayUrl);
            }
            if (CollectionUtils.isNotEmpty(urls)) {
                mEbayUrlService.addAll(urls);
            }
        }
    }

    private List<EbayUrl> extractCategory(Page page) {
        List<Selectable> selectables = page.getHtml().xpath("//*[@id='e1-1']/option").nodes();
        List<EbayUrl> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(selectables)) {
            EbayUrl url;
            for (Selectable selectable : selectables) {
                url = new EbayUrl();
                url.categoryName = selectable.xpath("/option/text()").get();
                url.url = selectable.xpath("/option/@value").get();

                if (url.url.equalsIgnoreCase("0")) {
                    continue;
                }

                url.url = "http://www.ebay.com/sch/i.html?_nkw=&_in_kw=1&_ex_kw=&_sacat=" + url.url + "&_udlo=&_udhi=&LH_BIN=1&LH_ItemCondition=3&_ftrt=901&_ftrv=1&_sabdlo=&_sabdhi=&_samilow=&_samihi=&_sadis=15&_stpos=510000&_sargn=-1%26saslc%3D1&_fsradio2=%26LH_LocatedIn%3D1&_salic=45&LH_SubLocation=1&_sop=12&_dmd=1&_ipg=200";
                url.type = 0;
                result.add(url);
            }
        }
        return result;
    }
}