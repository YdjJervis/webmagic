package us.codecraft.webmagic.samples.ttmeiju.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 排行榜
 */
public class SummaryPageProcessor implements PageProcessor {

    Logger logger = LoggerFactory.getLogger(SummaryPageProcessor.class);
    private Site mSite = Site.me().setRetryTimes(3);

    @Override
    public void process(Page page) {
        logger.info(page.getHtml().get());
    }

    @Override
    public Site getSite() {
        return mSite;
    }

    public static void main(String[] args) {
        Spider.create(new SummaryPageProcessor())
                .addUrl("http://www.ttmeiju.com/summary.html")
                .start();
    }
}
