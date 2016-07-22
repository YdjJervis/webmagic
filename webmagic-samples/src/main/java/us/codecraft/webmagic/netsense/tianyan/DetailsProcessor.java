package us.codecraft.webmagic.netsense.tianyan;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 天眼查详情页面
 */
public class DetailsProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

    @Override
    public void process(Page page) {
        System.out.println(page.getHtml());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new DetailsProcessor())
                .addUrl("http://www.tianyancha.com/company/1572266029")
                .setDownloader(new SeleniumDownloader("/home/hihi/opt/env/java/chromedriver"))
                .thread(1)
                .run();
    }
}
