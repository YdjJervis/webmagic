package us.codecraft.webmagic.netsense.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 全国500强企业
 */
public class TestProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3);

    @Override
    public void process(Page page) {

        System.out.println(page.getHtml());
    }

    @Override
    public Site getSite() {
        return site;
    }

    private static Spider mSpider = Spider
            .create(new TestProcessor())
            .thread(1);

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("A");

        list.add(0,"B");
        System.out.println(list);
    }

}