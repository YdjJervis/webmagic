package us.codecraft.webmagic.amazon.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.regex.Pattern;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/11/4 17:25
 */
public class ProductProcessor implements PageProcessor {

    @Override
    public void process(Page page) {
        /* 如果是产品首页 */
        if (Pattern.compile(".*/dp/.*").matcher(page.getUrl().get()).matches()) {

            String rootAsin = page.getHtml().xpath("//link[@rel='canonical']/@href").regex("/dp/(.*)").get();

            System.out.println(rootAsin);
        }
    }

    @Override
    public Site getSite() {
        return Site.me();
    }

    public static void main(String[] args) {
        Spider.create(new ProductProcessor()).addUrl("https://www.amazon.ca/dp/B00ZPN3O2I")
                .start();

    }
}