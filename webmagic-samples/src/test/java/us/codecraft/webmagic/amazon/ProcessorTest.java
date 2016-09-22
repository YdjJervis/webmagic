package us.codecraft.webmagic.amazon;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.pipeline.DiscussPipeline;

/**
 * 测试页面是否还能获取到数据
 */
public class ProcessorTest implements PageProcessor {

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(125);

    @Override
    public void process(Page page) {
        System.out.println("result");
        System.out.println(page.getHtml());
    }

    @Override
    public Site getSite() {
        mSite
                .addHeader("", "")
                .addHeader("", "")
                .addHeader("", "")
                .addHeader("", "")
                .addHeader("", "")
                .addHeader("", "")
                .addHeader("", "")
                .addHeader("", "")
                .addHeader("", "")
                .addHeader("", "")
                .addHeader("", "");
        mSite.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36 QIHU 360SE");
        return mSite;
    }

    public static void main(String[] args) {
        Spider.create(new ProcessorTest())
                .addPipeline(new DiscussPipeline())
                .thread(1)
//                .addUrl("https://www.amazon.cn/dp/B013SMD0PI")
                .addUrl("https://www.amazon.co.jp/product-reviews/B00SSRSY96")
                .start();
    }
}
