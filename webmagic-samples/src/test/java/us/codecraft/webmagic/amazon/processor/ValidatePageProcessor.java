package us.codecraft.webmagic.amazon.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 验证码页码测试
 * @date 2016/10/19 15:48
 */
public class ValidatePageProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        String amzn = page.getHtml().xpath("//input[@name='amzn']/@value").get();
        String amzn_r = page.getHtml().xpath("//input[@name='amzn-r']/@value").get();
        System.out.println(amzn + "  " + amzn_r);
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3).setSleepTime(3 * 1000).setTimeOut(10 * 1000);
    }

    public static void main(String[] args) {
        Spider mSpider = Spider.create(new ValidatePageProcessor())
                .addUrl("https://www.amazon.co.uk/errors/validateCaptcha?amzn=znNnUaJ7blcuz5pxdahjGA==&amzn-r=/dp/B01E9VRK26&field-keywords=NRATCL")
                .thread(1);
        mSpider.start();
    }
}