package com.eccang.amazon.processor;

import com.eccang.spider.base.util.UrlUtils;
import com.eccang.spider.ebay.pojo.EbayUrl;
import com.eccang.spider.ebay.pojo.SellerInfo;
import com.eccang.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/16 15:41
 */
@Service
public class EbayProcessorTest implements PageProcessor {

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(125);

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {

//        mSite.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//        mSite.addHeader("Accept-Encoding","gzip, deflate, sdch");
//        mSite.addHeader("Accept-Language","zh-CN,zh;q=0.8");
//        mSite.addHeader("Cache-Control","max-age=0");
//        mSite.addHeader("Connection","keep-alive");
//        mSite.addHeader("Cookie","__gads=ID=4cf90ed84d8bc605:T=1484552187:S=ALNI_MZKd3ufcOhz2g_mlUvROL1psH39lQ; cid=UcbjoWJgiqvk2L5q%231291442414; cssg=a63497461590a566a6233b35ff77224b; npii=btguid/a63497461590a566a6233b35ff77224b5a5db355^cguid/a634aa171590aa16e417608cfe4574e75a5db355^; JSESSIONID=BD8C1F07DA813FE59C3235357AA9AC8A; ns1=BAQAAAVmaRglKAAaAANgASVpdv4VjNjl8NjAxXjE0ODQ1NTIxODE1ODReXjFeM3wyfDV8NHw3XjFeMl40XjNeMTJeMTJeMl4xXjFeMF4xXjBeMV42NDQyNDU5MDc1SxMbWp1vwSggYkGf/RATSI8m5Uo*; s=BAQAAAVmaRglKAAWAAPgAIFh93YVhNjM0OTc0NjE1OTBhNTY2YTYyMzNiMzVmZjc3MjI0YgASAApYfd2FdGVzdENvb2tpZQDuASRYfd2FMwZodHRwOi8vd3d3LmViYXkuY29tL3NjaC9pLmh0bWw/X25rdz0mX2luX2t3PTEmX2V4X2t3PSZfc2FjYXQ9MjAwODEmX3VkbG89Jl91ZGhpPSZMSF9CSU49MSZMSF9JdGVtQ29uZGl0aW9uPTMmX2Z0cnQ9OTAxJl9mdHJ2PTEmX3NhYmRsbz0mX3NhYmRoaT0mX3NhbWlsb3c9Jl9zYW1paGk9Jl9zYWRpcz0xNSZfc3Rwb3M9NTEwMDAwJl9zYXJnbj0tMSUyNnNhc2xjJTNEMSZfZnNyYWRpbzI9JTI2TEhfTG9jYXRlZEluJTNEMSZfc2FsaWM9NDUmTEhfU3ViTG9jYXRpb249MSZfc29wPTEyJl9kbWQ9MSZfaXBnPTIwMD+uL/Kn5cGIKlVAeIJpMgPzKHiC; nonsession=BAQAAAVmaRglKAAaAAAgAHFikGQUxNDg0NTUzNDg2eDI2MjY4ODg3MzU3NXgweDJZADMAClpdv4U1MTAwMDAsQ0hOAMsAAVh8kw0yAMoAIGHijYVhNjM0OTc0NjE1OTBhNTY2YTYyMzNiMzVmZjc3MjI0YjIsgqajw3A1xTIx3Md44eIGCsfI; ds2=ssts/1484557322687^; ebay=%5Edv%3D587c7d23%5Esbf%3D%231000000000180000000210%5Ecos%3D3%5Ecv%3D15555%5Ejs%3D1%5E; dp1=btzo/-1e05c3ef30a^idm/1587dced3^u1p/QEBfX0BAX19AQA**5a5dbf85^bl/CN5c3ef305^pbf/#2000000e000e000008100020000045c3ef308^");
//        mSite.addHeader("Host","www.ebay.com");
//        mSite.addHeader("Upgrade-Insecure-Requests","1");
//        mSite.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
        return mSite;
    }

    private List<EbayUrl> extractCategory(Page page) {
        List<Selectable> selectables = page.getHtml().xpath("//*[@id='e1-1']/option").nodes();
        List<EbayUrl> result = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(selectables)) {
            EbayUrl url;
            for (Selectable selectable : selectables) {
                url = new EbayUrl();
                url.categoryName = selectable.xpath("/option/text()").get();
                url.url = selectable.xpath("/option/@value").get();

                if(url.url.equalsIgnoreCase("0") ) {
                    continue;
                }

                url.url = "http://www.ebay.com/sch/i.html?_nkw=&_in_kw=1&_ex_kw=&_sacat=" + url.url + "&_udlo=&_udhi=&LH_BIN=1&LH_ItemCondition=3&_ftrt=901&_ftrv=1&_sabdlo=&_sabdhi=&_samilow=&_samihi=&_sadis=15&_stpos=510000&_sargn=-1%26saslc%3D1&_fsradio2=%26LH_LocatedIn%3D1&_salic=45&LH_SubLocation=1&_sop=12&_dmd=1&_ipg=200";
                url.type = 0;
                result.add(url);
            }
        }

        return result;
    }



    private SellerInfo extractSellerInfo(Page page) {
        SellerInfo sellerInfo = new SellerInfo();

        if(page.getHtml().xpath("//*[@id='bsi-c']").nodes().size() == 0) {
            return sellerInfo;
        }
        sellerInfo.sellerName = page.getHtml().xpath("//*[@id='mbgLink']/span/text()").get();
        List<Selectable> selectables = page.getHtml().xpath("//*[@id='e12']//div[@class='bsi-c1']/div").nodes();

        if(CollectionUtils.isNotEmpty(selectables)) {
            StringBuffer sb = new StringBuffer();
            sb.append(page.getHtml().xpath("//*[@id='bsi-c']/div[@class='bsi-cnt']/div[@class='bsi-bn']/text()").get());
            for (Selectable selectable : selectables) {
                sb.append(selectable.xpath("/div/text()").get());
            }
            sellerInfo.address = sb.toString();
        }

        List<Selectable> selects = page.getHtml().xpath("//*[@id='e12']//div[@class='bsi-c2']/div").nodes();
        if(CollectionUtils.isNotEmpty(selects)) {
            for (Selectable select : selects) {
                String contactName = select.xpath("/div/span").nodes().get(0).xpath("/span/text()").get();
                String contactValue = select.xpath("/div/span").nodes().get(1).xpath("/span/text()").get();
                if(StringUtils.isNotEmpty(contactName) && StringUtils.isNotEmpty(contactValue)) {
                    if(contactName.toLowerCase().contains("phone")) {
                        sellerInfo.phone = contactValue;
                    } else if(contactName.toLowerCase().contains("email")) {
                        sellerInfo.email = contactValue;
                    } else if(contactName.toLowerCase().contains("fax")) {
                        sellerInfo.fax = contactValue;
                    }
                }
            }
        }
        return sellerInfo;
    }

    /**
     * 解析品类URL，并添加数据库
     */
    private void extractChildCategoryUrl(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='e1-6']/div[@class='rlp-b']//div[@class='cat-link ']/a").nodes();
        if(CollectionUtils.isNotEmpty(nodes)) {
            List<EbayUrl> urls = new ArrayList<>();
            EbayUrl url;
            for (Selectable node : nodes) {
                url = new EbayUrl();
                url.url = node.xpath("/a/@href").get();
                url.categoryName = node.xpath("/a/text()").get();
                url.type = 0;
                urls.add(url);
            }
        }
    }

    /**
     * 解析搜索到的产品数
     */
    private int extractProductsCount(Page page) {
        String productsCounts = page.getHtml().xpath("//*[@id='bciw']/div/span[@class='listingscnt']/text()").get();
        int productsCount = 0;
        if(StringUtils.isNotEmpty(productsCounts)) {
            productsCounts = productsCounts.replace("listings", "");
            if(productsCounts.contains(",")) {
                productsCounts = productsCounts.replace(",", "");
            }
            productsCount = Integer.valueOf(productsCounts.trim());
        }
        return productsCount;
    }

    /**
     * 解析产品listing页，除第一页外，并入库
     * @param index 页数
     */
    private void extractProductsListing(Page page, int index) {
        List<EbayUrl> urls = new ArrayList<>();
        EbayUrl url;
        String pageUrl = page.getHtml().xpath("//*[@id='Pagination']//td[@class='pages']/a").nodes().get(1).xpath("/a/@href").get();
        for (int i = 2; i < index; i++) {
            url = new EbayUrl();
            url.url = UrlUtils.setValue(pageUrl, "_pgn", String.valueOf(i));
//            url.categoryName = getUrl(page).categoryName;
            url.type = 1;
            url.toString();
            urls.add(url);
        }
    }

    /**
     * 解析产品url，并存入数据库中
     */
    private void extractProductsUrl(Page page) {
        List<Selectable> selectables = page.getHtml().xpath("//*[@id='ListViewInner']/li/h3[@class='lvtitle']/a").nodes();
        if(CollectionUtils.isNotEmpty(selectables)) {
            List<EbayUrl> urls = new ArrayList<>();
            EbayUrl ebayUrl;
            for (Selectable selectable : selectables) {
                ebayUrl = new EbayUrl();
                ebayUrl.url = selectable.xpath("/a/@href").get();
                ebayUrl.type = 2;
//                ebayUrl.categoryName = getUrl(page).categoryName;
                ebayUrl.toString();
                urls.add(ebayUrl);
            }
        }
    }

    public static void main(String[] args) {
        Request request = new Request("http://www.ebay.com/sch/Childrens-Jewelry/84605/i.html?_nkw=&_udlo=&_udhi=&LH_BIN=1&LH_ItemCondition=3&_ftrt=901&_ftrv=1&_sabdlo=&_sabdhi=&_samilow=&_samihi=&_sadis=15&_stpos=510000&_sop=12&_dmd=1&_ipg=200&LH_LocatedIn=45");
        Spider.create(new EbayProcessorTest())
                .thread(1)
                .addRequest(request)
                .start();
    }
}