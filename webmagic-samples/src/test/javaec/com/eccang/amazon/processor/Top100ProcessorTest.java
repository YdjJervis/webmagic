package com.eccang.amazon.processor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.base.util.UrlUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试页面是否还能获取到数据
 */
public class Top100ProcessorTest implements PageProcessor {

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(125);

    @Override
    public void process(Page page) {
//        extractTop100ProductInfo(page);
        System.out.println(page.getHtml().xpath("//li[@id='zg_tabTitle' and @class='active']/h3/text()").get());
    }

    @Override
    public Site getSite() {
        mSite.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        mSite.addHeader("Accept-Encoding", "gzip, deflate, sdch, br");
        mSite.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        mSite.addHeader("Cache-Control", "max-age=0");
        mSite.addHeader("Connection", "keep-alive");
        mSite.addHeader("Cookie", "x-wl-uid=1S3UB0PWRfWVtoMS0L5JeBl+mARwPVccIThGcklbaHfdKvGRhYYSWH+vQ/sXY9tB9H52mQdkhbcY=; session-token=YqXmKsodz92qG5UBbyIdxAVzRJoVfkvECfv26X8QOXchNrjqG+mtFeaLc96hZEZOkQ7QmY4fqVhvSUz8fyfrB+ZnlUYl28vSsP012zrXe/wlC0LgNiThhcAvQzTrbDaHR9TE2H3WssXdRC1l+7SltTwMHDruu6eCzZCVVeU9sVEaNc5VXDq4K4hojoOADmNpTrSHFhq3A11/IrOgCVe3wud6JXohsHz83N7wLzuUBIPC6fgxuRVXN0A+DssiyqbR; skin=noskin; JSESSIONID=7B64AC7E1F8D360029513E7971C6E15C; ubid-main=162-5971175-8414817; session-id-time=2082787201l; session-id=167-8229845-7101626; csm-hit=112NPSAWX27YV622TV18+s-68BSFQSS9VHVSGWQZ6QG|1484532663381");
        mSite.addHeader("Host", "www.amazon.com");
        mSite.addHeader("Upgrade-Insecure-Requests", "1");
        mSite.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");

        return mSite;
    }


    private void extractTop100ProductInfo(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='zg_centerListWrapper']/div[@class='zg_itemImmersion']").nodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            for (Selectable node : nodes) {
                String rankNumStr = node.xpath("//div[@class='zg_rankDiv']/span/text()").get();
                int rankNum = 0;
                if (StringUtils.isNotEmpty(rankNumStr)) {
                    rankNum = Integer.valueOf(rankNumStr.trim().replace(".", ""));
                }
                String productName = node.xpath("//div[@class='zg_itemWrapper']/div/a/text()").get();
                if (StringUtils.isEmpty(productName)) {
                    productName = node.xpath("//div[@class='zg_itemWrapper']/div/a/span/@title").get();
                }
                String productImgUrl = node.xpath("//div[@class='zg_itemWrapper']/div//img/@src").get();
                String productUrl = node.xpath("//div[@class='zg_itemWrapper']/div/a/@href").get();
                String reviewStar = node.xpath("//div[@class='zg_itemWrapper']/div/div[contains(@class,'a-icon-row')]//span[@class='a-icon-alt']/text()").get();
                String reviewNum = node.xpath("//div[@class='zg_itemWrapper']/div/div[contains(@class,'a-icon-row')]/a[contains(@class,'a-size-small')]/text()").get();
                String productPrice = node.xpath("//*[@class='p13n-sc-price']/text()").get();
                String amazonDelivery = node.xpath("//*[@class='a-icon-prime']/span[@class='a-icon-alt']/text()").get();
                System.out.println(rankNum);
                System.out.println(productName);
                System.out.println(productImgUrl);
                System.out.println(productUrl);
                System.out.println(reviewStar);
                System.out.println(reviewNum);
                System.out.println(productPrice);
                System.out.println(amazonDelivery);
            }
        }
    }

    private List<Url> extractTop100ProductListing(Page page) {
        List<Url> urls = new ArrayList<>();
        List<Selectable> nodes = page.getHtml().xpath("//*[@id='zg_paginationWrapper']/ol/li").nodes();
        if (CollectionUtils.isNotEmpty(nodes)) {
            Url url;
            for (Selectable node : nodes) {
                url = new Url();
                String urlStr = node.xpath("/li/a/@href").get();
                if (UrlUtils.getValue(urlStr, "pg").equalsIgnoreCase("1")) {
                    continue;
                }
                url.urlMD5 = UrlUtils.md5(urlStr);
                url.url = urlStr;
                url.type = R.CrawlType.TOP_100_PRODUCT;
                urls.add(url);
            }
        }
        return urls;
    }

    public static void main(String[] args) {
        //统计库存
//        Request request = new Request("https://www.amazon.com/gp/cart/ajax-update.html/ref=ox_sc_update_quantity_1%7C50%7C60");
        //放入购物车
//        Request request = new Request("https://www.amazon.com/gp/product/handle-buy-box/ref=dp_start-bbf_1_glance");

//        Request request = new Request("https://www.amazon.com/bestsellers");
        Request request = new Request("https://www.amazon.com/Best-Sellers-Appstore-Android/zgbs/mobile-apps/ref=zg_bs_nav_0");
        Spider.create(new Top100ProcessorTest())
                .thread(1)
                .addRequest(request)
                .start();
    }
}
