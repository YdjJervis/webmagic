package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.pipeline.DiscussPipeline;
import us.codecraft.webmagic.samples.amazon.pojo.Country;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;
import us.codecraft.webmagic.samples.amazon.pojo.UrlPrefix;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论
 */
public class DiscussProcessor implements PageProcessor {

    private static final String ASIN = "asin";
    private static Country mCountry = UrlPrefix.getCountry("jp");

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(250);

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public void process(Page page) {
        dealProductDetails(page);
        dealAllDiscuss(page);
    }

    private void dealProductDetails(Page page) {
        if (page.getUrl().get().startsWith(mCountry.getProductUrl())) {
            String productID = page.getUrl().get().replace(mCountry.getProductUrl(), "");
            String url = mCountry.getDiscussUrl() + productID;
            Request request = new Request(url);
            request.putExtra(ASIN, productID);

            page.addTargetRequest(request);
        }
    }

    private void dealAllDiscuss(Page page) {
        if (page.getUrl().get().contains("product-reviews")) {
            List<Selectable> discussNodeList = page.getHtml().xpath("//div[@class='a-section review']").nodes();

            String asin = (String) page.getRequest().getExtra(ASIN);

            List<Discuss> discussList = new ArrayList<Discuss>();
            for (Selectable discussNode : discussNodeList) {
                String star = discussNode.xpath("//span[@class='a-icon-alt']/text()").get();
                String title = discussNode.xpath("//a[@class='a-size-base a-link-normal review-title a-color-base a-text-bold']/text()").get();
                String person = discussNode.xpath("//a[@class='a-size-base a-link-normal author']/text()").get();
                String personID = discussNode.xpath("//a[@class='a-size-base a-link-normal author']/@href").regex(".*profile/(.*)/ref.*").get();
                String time = discussNode.xpath("//span[@class='a-size-base a-color-secondary review-date']/text()").get();
                String version = discussNode.xpath("//a[@class='a-size-mini a-link-normal a-color-secondary]/text()").get();
                String content = discussNode.xpath("//span[@class='a-size-base review-text]/text()").get();
                String buyStatus = discussNode.xpath("//span[@class='a-size-mini a-color-state a-text-bold]/text()").get();

                Discuss discuss = new Discuss();
                discuss.setAsin(asin);
                discuss.setContent(content);
                discuss.setPerson(person);
                discuss.setPersonID(personID);
                discuss.setTime(time);
                discuss.setStar(star);
                discuss.setTitle(title);
                discuss.setVersion(version);
                discuss.setBuyStatus(buyStatus);
                logger.info(discuss.toString());

                discussList.add(discuss);
            }
            page.putField(DiscussPipeline.PARAM_LIST, discussList);

            List<String> pageUrlList = page.getHtml().xpath("//li[@class='page-button']/a/@href").all();
            for (String pageUrl : pageUrlList) {
                Request request = new Request(pageUrl);
                request.putExtra(ASIN, asin);
                page.addTargetRequest(request);
            }
        }
    }

    @Override
    public Site getSite() {
        mSite.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36 QIHU 360SE");
        return mSite;
    }

    public static void main(String[] args) {
//        Spider.create(new DiscussProcessor())
//                .addPipeline(new DiscussPipeline())
//                .thread(1)
////                .addUrl("https://www.amazon.cn/dp/B013SMD0PI")
//                .addUrl(mCountry.getProductUrl() + "B01JIB1LRW")
//                .start();
        new DiscussProcessor().logger.info("hello");
    }
}
