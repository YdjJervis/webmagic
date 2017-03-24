package com.eccang.amazon.processor;

import com.eccang.wsclient.validate.ImageOCRService;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 验证码页码测试
 * @date 2016/10/19 15:48
 */
public class ValidatePageProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        String validateUrl = page.getHtml().xpath("//div[@class='a-row a-text-center']/img/@src").get();
        ImageOCRService service = new ImageOCRService();
        String code = null;
        try {
            Date startTime = new Date();
            code = service.getBasicHttpBindingIImageOCRService().getVerCodeFromUrl(validateUrl, "review");
            Date endTime = new Date();
            System.out.println(endTime.getTime() - startTime.getTime());
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(code);
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3).setSleepTime(3 * 1000).setTimeOut(10 * 1000);
    }

    public static void main(String[] args) {
        Spider mSpider = Spider.create(new ValidatePageProcessor())
                .addUrl("https://www.amazon.de/errors/validateCaptcha?amzn=NH0lq3sAaaV6lYDg61tYrw==&amzn-r=/gp/bestsellers/books/3118971/ref=zg_bs_pg_3/253-7841937-6240755?ie=UTF8&field-keywords=YAGFEN")
                .thread(1);
        mSpider.start();
    }
}