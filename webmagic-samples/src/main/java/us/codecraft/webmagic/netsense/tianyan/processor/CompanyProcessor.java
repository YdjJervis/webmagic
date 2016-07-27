package us.codecraft.webmagic.netsense.tianyan.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.netsense.tianyan.pipeline.CompanyPipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 检索天眼查网上所有的公司的处理器
 */
public class CompanyProcessor implements PageProcessor {

    Site mSite = Site.me().setRetryTimes(3);

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return mSite;
    }

    public static void main(String[] args) {
        Spider spider = Spider.create(new CompanyProcessor())
                .addPipeline(new CompanyPipeline())
                .addUrl(null)
                .thread(3);
        spider.start();
    }

    private String getUrl(String keyword) {
        //http://www.tianyancha.com/suggest/a.json
        return "http://www.tianyancha.com/suggest/" + keyword + ".json";
    }
}
