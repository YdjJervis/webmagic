package us.codecraft.webmagic.amazon.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.extractor.followsell.FollowSellExtractorAdapter;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.FollowSell;
import us.codecraft.webmagic.samples.base.util.UserAgentUtil;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 产品跟卖
 * @date 2016/11/4 17:02
 */
public class FollowSellProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        List<FollowSell> followSellList = new FollowSellExtractorAdapter().extract("DE", extractAsin(page), page);
        System.out.println(followSellList);
    }

    @Override
    public Site getSite() {
        return Site.me().setUserAgent(UserAgentUtil.getRandomUserAgent());
    }

    private String extractAsin(Page page) {
        return page.getUrl().regex(".*/gp/offer-listing/([0-9A-Za-z]*)").get();
    }

    public static void main(String[] args) {
        //eg:
        //us https://www.amazon.com/gp/offer-listing/B019ZCMROK
        //uk https://www.amazon.co.uk/gp/offer-listing/B0186FESVC
        //de https://www.amazon.de/gp/offer-listing/B01ARHAWSA 有翻页
        Spider.create(new FollowSellProcessor()).addUrl("https://www.amazon.de/gp/offer-listing/B01ARHAWSA")
                .start();
    }
}