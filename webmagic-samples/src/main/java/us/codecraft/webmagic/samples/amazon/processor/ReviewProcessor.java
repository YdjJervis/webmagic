package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.monitor.ReviewUrlMonitor;
import us.codecraft.webmagic.samples.amazon.pipeline.ReviewPipeline;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.SiteService;
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
    private ReviewUrlMonitor mReviewUrlMonitor;
    @Autowired
    private SiteService mSiteService;
    @Autowired
    private ReviewPipeline mReviewPipeline;

    private ApplicationContext mContext;

    private static final String URL_EXTRA = "url_extra";

    @Autowired
    private UrlService mUrlService;

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(3000).setTimeOut(2000);

    private Logger mLogger = Logger.getLogger(getClass());

    @Override
    public void process(Page page) {
        mReviewUrlMonitor.setPage(page);

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
            page.addTargetRequest(request);
        }
    }

    private void dealAllReview(Page page) {
        if (page.getUrl().get().contains(Review.PRODUCT_REVIEWS)) {

            updateUrlStatus(page);

            List<Selectable> reviewNodeList = page.getHtml().xpath("//div[@class='a-section review']").nodes();

            String asin = page.getUrl().regex(".*product-reviews/(.*?)/.*").get();//URL中提取asin码
            if (StringUtils.isEmpty(asin)) {//提取失败,更改规则重提取
                asin = page.getUrl().regex(".*product-reviews/(.*)").get();
            }
            String domain = page.getUrl().regex("(https://www.amazon.*?)/.*").get();
            String siteCode = mSiteService.findByDomain(domain).basCode;

            mLogger.info("解析 " + domain + " 站点下ASIN码为 " + asin + " 的评论信息,当前URL=" + page.getUrl());

            List<Review> reviewList = new ArrayList<Review>();
            for (Selectable reviewNode : reviewNodeList) {
                int star = Integer.valueOf(reviewNode.xpath("//*[@data-hook='review-star-rating']/@class").regex(".*-([0-5]).*").get());//提取星级
                String title = reviewNode.xpath("//a[@class='a-size-base a-link-normal review-title a-color-base a-text-bold']/text()").get();
                String reviewId = reviewNode.xpath("//a[@class='a-size-base a-link-normal review-title a-color-base a-text-bold']/@href").regex(".*customer-reviews/(.*)/ref.*").get();
                String person = reviewNode.xpath("//a[@class='a-size-base a-link-normal author']/text()").get();
                String personID = reviewNode.xpath("//a[@class='a-size-base a-link-normal author']/@href").regex(".*profile/(.*)/ref.*").get();
                String time = reviewNode.xpath("//span[@class='a-size-base a-color-secondary review-date']/text()").get();
                String version = reviewNode.xpath("//a[@class='a-size-mini a-link-normal a-color-secondary]/text()").get();
                String content = reviewNode.xpath("//span[@class='a-size-base review-text]/text()").get();
                String buyStatus = reviewNode.xpath("//span[@class='a-size-mini a-color-state a-text-bold]/text()").get();

                Review review = new Review();
                review.basCode = siteCode;
                review.saaAsin = asin;
                review.sarStar = star;
                review.sarTitle = title;
                review.sarPersonId = personID;
                review.sarReviewId = reviewId;
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
            mLogger.info("新提取的翻页Url如下：");
            mLogger.info(pageUrlList);

            List<Url> urlList = new ArrayList<Url>();
            for (String pageUrl : pageUrlList) {
                Matcher matcher = Pattern.compile("(.*amazon.*?/).*(product-reviews.*)").matcher(pageUrl);
                if (matcher.find()) {

                    String urlStr = matcher.group(1) + matcher.group(2);
                    Url url = new Url();
                    url.siteCode = siteCode;
                    url.parentUrl = page.getUrl().get();
                    url.url = urlStr;

                    urlList.add(url);
                }
            }
            page.putField(ReviewPipeline.PARAM_URL, urlList);
        }
    }

    private void updateUrlStatus(Page page) {

        Url url = (Url) page.getRequest().getExtra(URL_EXTRA);
        int statusCode = page.getStatusCode();
        mLogger.info("当前页面:" + page.getUrl() + " 爬取状态：" + statusCode);

        url.status = statusCode;
        mLogger.info("改变状态后的Url对象：" + url);

        mUrlService.update(url);
    }

    @Override
    public Site getSite() {
        mLogger.debug("getSite():::");
        mSite.setUserAgent(UserAgentUtil.getRandomUserAgent());
        return mSite;
    }

    @Override
    public void execute() {
        mLogger.info("开始执行爬取任务...");

        Spider mSpider = Spider.create(this)
                .addPipeline(mReviewPipeline)
                .thread(1);

        List<Url> urlList = mUrlService.findFailures();

        mLogger.info("找到状态码不为200的Url个数：" + urlList.size());
        if (CollectionUtils.isNotEmpty(urlList)) {
            for (Url url : urlList) {
                Request request = new Request(url.url);
                request.putExtra(URL_EXTRA, url);
                mSpider.addRequest(request);
            }
        }

        mLogger.info("开始爬取...");
        mSpider.start();
    }

}
