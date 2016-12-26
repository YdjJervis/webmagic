package us.codecraft.webmagic.amazon.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.extractor.followsell.FollowSellExtractorAdapter;
import us.codecraft.webmagic.samples.amazon.pojo.FollowSell;
import us.codecraft.webmagic.samples.base.util.UserAgentUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 产品跟卖
 * @date 2016/11/4 17:02
 */
public class FollowSellProcessor implements PageProcessor {


    @Override
    public void process(Page page) {
        /* 如果是产品首页 */
        if (Pattern.compile(".*/gp/offer-listing/.*").matcher(page.getUrl().get()).matches()) {

            List<FollowSell> followSellList = new FollowSellExtractorAdapter().extract("US", extractAsin(page), page);
            System.out.println(followSellList);
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setUserAgent(UserAgentUtil.getRandomUserAgent());
    }

    String extractAsin(Page page) {
        return page.getUrl().regex(".*/gp/offer-listing/([0-9A-Za-z]*)").get();
    }

    public static void main(String[] args) {

        Spider.create(new FollowSellProcessor()).addUrl("https://www.amazon.com/gp/offer-listing/B019ZCMROK")
                .start();
    }
}