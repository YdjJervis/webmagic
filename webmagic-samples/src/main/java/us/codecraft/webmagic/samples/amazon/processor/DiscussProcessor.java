package us.codecraft.webmagic.samples.amazon.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.pipeline.DiscussPipeline;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论
 */
public class DiscussProcessor implements PageProcessor {

    public static final String PREFIX_PRODUCT_DETAILS = "https://www.amazon.cn/dp/";
    public static final String PREFIX_ALL_DISCUSS = "https://www.amazon.cn/product-reviews/";

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(1000);


    @Override
    public void process(Page page) {
        dealProductDetails(page);
        dealAllDiscuss(page);
    }

    private void dealProductDetails(Page page) {
        if (page.getUrl().get().startsWith(PREFIX_PRODUCT_DETAILS)) {
            String productID = page.getUrl().get().replace(PREFIX_PRODUCT_DETAILS, "");
            page.addTargetRequest(PREFIX_ALL_DISCUSS + productID);
        }
    }

    private void dealAllDiscuss(Page page) {
        if (page.getUrl().get().contains("product-reviews")) {
            List<Selectable> discussNodeList = page.getHtml().xpath("//div[@class='a-section review']").nodes();

            List<Discuss> discussList = new ArrayList<Discuss>();
            for (Selectable discussNode : discussNodeList) {
                String star = discussNode.xpath("//span[@class='a-icon-alt']/text()").get();
                String title = discussNode.xpath("//a[@class='a-size-base a-link-normal review-title a-color-base a-text-bold']/text()").get();
                String person = discussNode.xpath("//a[@class='a-size-base a-link-normal author']/text()").get();
                String time = discussNode.xpath("//span[@class='a-size-base a-color-secondary review-date']/text()").get();
                String version = discussNode.xpath("//a[@class='a-size-mini a-link-normal a-color-secondary]/text()").get();
                String content = discussNode.xpath("//span[@class='a-size-base review-text]/text()").get();
                String buyStatus = discussNode.xpath("//span[@class='a-size-mini a-color-state a-text-bold]/text()").get();

                Discuss discuss = new Discuss();
                discuss.setContent(content);
                discuss.setPerson(person);
                discuss.setTime(time);
                discuss.setStar(star);
                discuss.setTitle(title);
                discuss.setVersion(version);
                discuss.setBuyStatus(buyStatus);

                discussList.add(discuss);
            }
            page.putField(DiscussPipeline.PARAM_LIST, discussList);

            List<String> pageUrlList = page.getHtml().xpath("//li[@class='page-button']/a/@href").all();
            for (String pageUrl : pageUrlList) {
                page.addTargetRequest(pageUrl);
            }
        }
    }

    @Override
    public Site getSite() {
        return mSite;
    }

    public static void main(String[] args) {
        Spider.create(new DiscussProcessor())
                .addPipeline(new DiscussPipeline())
                .thread(1)
                .addUrl("https://www.amazon.cn/dp/B013SMD0PI")
                .start();
    }
}
