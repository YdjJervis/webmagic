package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.pipeline.BannerPipeline;
import us.codecraft.webmagic.samples.amazon.pojo.Country;
import us.codecraft.webmagic.samples.amazon.pojo.UrlPrefix;
import us.codecraft.webmagic.samples.base.util.PageUtil;

import java.util.regex.Pattern;

/**
 * 评论
 */
public class ProductProcessor implements PageProcessor {


    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(250);

    private Logger logger = Logger.getLogger(getClass());
    private static Country mCountry = UrlPrefix.getCountry("cn");

    @Override
    public void process(Page page) {
        dealBanner(page);
    }

    private void dealBanner(Page page) {
        dealProduct(page);
    }

    private void dealProduct(Page page) {

        if (isProductPage(page)) {
            PageUtil.savePage(page, "C:\\Users\\Administrator\\Desktop\\爬虫\\amazon");
        }
    }

    private boolean isProductPage(Page page) {
        return Pattern.compile(mCountry.getProductUrl() + "[0-9a-zA-Z]+").matcher(page.getUrl().get()).matches();
    }

    @Override
    public Site getSite() {
        mSite.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36 QIHU 360SE");
        return mSite;
    }

    public static void main(String[] args) {

        Spider.create(new ProductProcessor())
                .addPipeline(new BannerPipeline())
                .thread(1)
                .addUrl(mCountry.getProductUrl() + "B00HTSBDKC")
                .start();

    }
}
