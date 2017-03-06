package com.eccang.spider.ebay.processor;

import com.eccang.spider.amazon.R;
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
 * 2017/1/17 9:47
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

//        List<EbayUrl> ebayUrls = extractCategory(page);
//        mEbayUrlService.addAll(ebayUrls);

        if (url.type == 0) {
            /*解析品类URL，并添加数据库*/
            extractChildCategoryUrl(page);

            /*解析搜索到的产品数*/
            int productsCount = extractProductsCount(page);

            /*计算页数,并将除第一页外的所有URL添加到数据库中*/
            if (productsCount != 0) {
                int index = productsCount % 200 > 0 ? (productsCount / 200) + 1 : productsCount / 200;
                if (index > 1) {
                    extractProductsListing(page, index+1);
                }

                /*解析品类下第一页的产品url，并存入数据库中*/
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

        List<Selectable> selectables = page.getHtml().xpath("//*[@id='LeftNavCategoryContainer']//div[@class='rlp-b']//div[@class='cat-link']").nodes();

        if (CollectionUtils.isNotEmpty(selectables)) {
            String str;
            String productNum;
            boolean isHasChildNode = false;
            EbayUrl url;
            for (int i = 0; i < selectables.size(); i++) {
                str = selectables.get(i).xpath("/div/span[@class='cnt']/text()").get();
                productNum = RegexUtil.reg(str, "([0-9]{1,}[.]?[0-9]*)");
                if (StringUtils.isNotEmpty(productNum) && !productNum.equalsIgnoreCase("0")) {
                    isHasChildNode = true;

                    url = new EbayUrl();
                    url.url = selectables.get(i).xpath("/div/a/@href").get();
                    url.urlMD5 = UrlUtils.md5(url.url);
                    if (mEbayUrlService.isExist(url.urlMD5)) {
                        sLogger.info("============= url(" + url.url + ") has existed.");
                        continue;
                    }
                    url.categoryName = selectables.get(i).xpath("/div/a/text()").get();
                    url.siteCode = getUrl(page).siteCode;
                    url.type = 0;
                    mEbayUrlService.add(url);
                }
            }

            if(!isHasChildNode) {
                sLogger.info("品类（" + getUrl(page).categoryName + ")下没有子品类存在；url:" + getUrl(page).url);
            }
        }
    }

    /**
     * 解析搜索到的产品数
     */
    private int extractProductsCount(Page page) {
        EbayUrl ebayUrl = getUrl(page);

        String productsCounts;
        if(ebayUrl.siteCode.equalsIgnoreCase(R.SiteCode.NL) || ebayUrl.siteCode.equalsIgnoreCase(R.SiteCode.PL)) {
            productsCounts = page.getHtml().xpath("//*[@id='cbelm']/div[@class='clt']/h1/span[@class='rcnt']/text()").get();
        } else {
            productsCounts = page.getHtml().xpath("//*[@id='bciw']/div/span[@class='listingscnt']/text()").get();
        }


        int productsCount = 0;
        if (StringUtils.isNotEmpty(productsCounts)) {
            productsCounts = RegexUtil.reg(productsCounts, "([0-9]{1,}[,]?[0-9]*)");
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
        index = index > 50 ? 50 : index;
        List<EbayUrl> urls = new ArrayList<>();
        EbayUrl url;
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='Pagination']//td[@class='pages']/a").nodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            String pageUrl = nodes.get(1).xpath("/a/@href").get();
            for (int i = 2; i < index; i++) {
                url = new EbayUrl();
                url.url = UrlUtils.setValue(pageUrl, "_pgn", String.valueOf(i));
                url.url = UrlUtils.setValue(url.url, "_skc", String.valueOf((i-1)*200));
                url.urlMD5 = UrlUtils.md5(url.url);
                url.categoryName = getUrl(page).categoryName;
                url.siteCode = getUrl(page).siteCode;
                url.type = 1;
                urls.add(url);
            }
            if (CollectionUtils.isNotEmpty(urls)) {
                for (EbayUrl ebayUrl : urls) {
                    mEbayUrlService.add(ebayUrl);
                }
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
                if (mEbayUrlService.isExist(ebayUrl.urlMD5)) {
                    sLogger.info("============= urlMD5(" + ebayUrl.urlMD5 + "),url(" + ebayUrl.url + ") has existed.");
                    continue;
                }
                ebayUrl.type = 2;
                ebayUrl.categoryName = getUrl(page).categoryName;
                ebayUrl.siteCode = getUrl(page).siteCode;
                urls.add(ebayUrl);
            }
            if (CollectionUtils.isNotEmpty(urls)) {
                for (EbayUrl url : urls) {
                    mEbayUrlService.add(url);
                }
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

                url.url = "http://www.ebay.ph/sch/i.html?_nkw=&_in_kw=1&_ex_kw=&_sacat="+url.url+"&_udlo=&_udhi=&LH_BIN=1&LH_ItemCondition=3&_ftrt=901&_ftrv=1&_sabdlo=&_sabdhi=&_samilow=&_samihi=&_sadis=10&_fpos=&LH_SubLocation=1&_sargn=-1%26saslc%3D0&_fsradio2=%26LH_LocatedIn%3D1&_salic=45&_saact=162&LH_SALE_CURRENCY=0&_sop=12&_dmd=1&_ipg=200";
                url.siteCode = getUrl(page).siteCode;
                url.urlMD5 = UrlUtils.md5(url.url);
                url.type = 0;
                result.add(url);
            }
        }
        return result;
    }
}