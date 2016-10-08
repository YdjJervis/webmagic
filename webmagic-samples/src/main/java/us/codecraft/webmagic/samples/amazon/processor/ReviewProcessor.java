package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.collections.CollectionUtils;
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
import us.codecraft.webmagic.samples.amazon.pipeline.ReviewPipeline;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;
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
public class ReviewProcessor implements PageProcessor, ScheduledTask {

    @Autowired
    private ReviewlUrlMonitor mReviewlUrlMonitor;

    @Autowired
    private UrlService mUrlService;

    private static final String ASIN = "asin";

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(3000).setTimeOut(2000);

    private Logger mLogger = Logger.getLogger(getClass());

    @Override
    public void process(Page page) {
        mReviewlUrlMonitor.setPage(page);

        dealAllReview(page);
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

    private void dealAllReview(Page page) {
        if (page.getUrl().get().contains(Review.PRODUCT_REVIEWS)) {
            List<Selectable> reviewNodeList = page.getHtml().xpath("//div[@class='a-section review']").nodes();

            String asin = (String) page.getRequest().getExtra(ASIN);

            List<Review> reviewList = new ArrayList<Review>();
            for (Selectable reviewNode : reviewNodeList) {
                String star = reviewNode.xpath("//span[@class='a-icon-alt']/text()").get();
                String title = reviewNode.xpath("//a[@class='a-size-base a-link-normal review-title a-color-base a-text-bold']/text()").get();
                String person = reviewNode.xpath("//a[@class='a-size-base a-link-normal author']/text()").get();
                String personID = reviewNode.xpath("//a[@class='a-size-base a-link-normal author']/@href").regex(".*profile/(.*)/ref.*").get();
                String time = reviewNode.xpath("//span[@class='a-size-base a-color-secondary review-date']/text()").get();
                String version = reviewNode.xpath("//a[@class='a-size-mini a-link-normal a-color-secondary]/text()").get();
                String content = reviewNode.xpath("//span[@class='a-size-base review-text]/text()").get();
                String buyStatus = reviewNode.xpath("//span[@class='a-size-mini a-color-state a-text-bold]/text()").get();

                Review review = new Review();
                review.basCode = "";
                review.saaAsin = asin;
                review.sarStar = star;
                review.sarTitle = title;
                review.sarPersonId = personID;
                review.sarPerson = person;
                review.sarTime = time;
                review.sarVersion = version;
                review.sarContent = content;
                review.sarBuyStatus = buyStatus;
                mLogger.info(review);

                reviewList.add(review);
            }
            page.putField(ReviewPipeline.PARAM_LIST, reviewList);

            List<String> pageUrlList = page.getHtml().xpath("//li[@class='page-button']/a/@href").all();
            for (String pageUrl : pageUrlList) {
                Matcher matcher = Pattern.compile("(.*amazon.*?/).*(product-reviews.*)").matcher(pageUrl);
                if (matcher.find()) {

                    String urlStr = matcher.group(1) + matcher.group(2);
                    Url url = new Url();
                    url.parentUrl = page.getUrl().get();
                    url.url = urlStr;

                    page.putField(ReviewPipeline.PARAM_URL, url);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        mLogger.debug("getSite():::");
        mSite.setUserAgent(UserAgentUtil.getRandomUserAgent());
        return mSite;
    }

    private static Spider mSpider = Spider.create(new ReviewProcessor())
            .addPipeline(new ReviewPipeline())
            .thread(1);

    @Override
    public void execute() {
        List<Url> urlList = mUrlService.findFailures();
        if (CollectionUtils.isNotEmpty(urlList)) {
            for (Url url : urlList) {
                mSpider.addUrl(url.url);
            }
        }
        mSpider.start();
    }
}
