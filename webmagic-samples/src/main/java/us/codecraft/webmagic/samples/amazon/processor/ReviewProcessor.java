package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.monitor.ReviewlUrlMonitor;
import us.codecraft.webmagic.samples.amazon.pipeline.DiscussPipeline;
import us.codecraft.webmagic.samples.amazon.pojo.Country;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;
import us.codecraft.webmagic.samples.amazon.pojo.UrlPrefix;
import us.codecraft.webmagic.samples.base.util.PageUtil;
import us.codecraft.webmagic.samples.base.util.UrlUtils;
import us.codecraft.webmagic.samples.base.util.UserAgentUtil;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 评论
 */
@Service
public class ReviewProcessor implements PageProcessor{

    @Autowired
    private ReviewlUrlMonitor mReviewlUrlMonitor;

    private static final String ASIN = "asin";
    private static Country mCountry = UrlPrefix.getCountry("es");

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(3000).setTimeOut(2000);

    private Logger logger = Logger.getLogger(getClass());

    @Override
    public void process(Page page) {
        mReviewlUrlMonitor.setPage(page);

        dealProductDetails(page);
        dealAllDiscuss(page);
        dealValidate(page);

    }

    private void dealValidate(Page page) {
        String validateUrl = page.getHtml().xpath("//div[@class='a-row a-text-center']/img/@src").get();
        if (StringUtils.isNotEmpty(validateUrl)) {
            PageUtil.saveImage(validateUrl, "C:\\Users\\Administrator\\Desktop\\爬虫\\amazon\\验证码");

            String value = UrlUtils.getValue(page.getUrl().get(), "flag");
            if (StringUtils.isEmpty(value)) {
                value = "0";
            }
            String newUrl = UrlUtils.setValue(page.getUrl().get(), "flag", String.valueOf(Integer.valueOf(value) + 1));

            Request request = new Request(newUrl);
            request.putExtra(ASIN, page.getRequest().getExtra(ASIN));
            page.addTargetRequest(request);
        }
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
                Matcher matcher = Pattern.compile("(.*amazon.*?/).*(product-reviews.*)").matcher(pageUrl);
                if (matcher.find()) {
                    Request request = new Request(matcher.group(1) + matcher.group(2));
                    request.putExtra(ASIN, asin);
                    page.addTargetRequest(request);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        logger.debug("getSite():::");
        mSite.setUserAgent(UserAgentUtil.getRandomUserAgent());
        return mSite;
    }

    private static Spider mSpider = Spider.create(new ReviewProcessor())
            .addPipeline(new DiscussPipeline())
            .addUrl(mCountry.getProductUrl() + "B01E7JP428")
            .thread(1);

    public static void main(String[] args) {
        mSpider.start();
    }

}
