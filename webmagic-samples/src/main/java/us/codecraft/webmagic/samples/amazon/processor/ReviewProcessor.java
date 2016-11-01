package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.ReviewService;
import us.codecraft.webmagic.samples.amazon.util.ReviewTimeUtil;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;
import us.codecraft.webmagic.samples.base.util.UrlUtils;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论爬取Processor，定时任务，负责对未爬取的Url进行爬取和信息抽取，入库等逻辑
 * @date 2016/10/11
 */
@Service
public class ReviewProcessor extends BasePageProcessor implements ScheduledTask {

    @Autowired
    protected ReviewService mReviewService;

    @Override
    protected void dealReview(Page page) {

        String currentUrl = page.getUrl().get();

        List<Selectable> reviewNodeList = extractReviewNodeList(page);

        String asin = extractAsin(page);
        String siteCode = extractSite(page).basCode;

        sLogger.info("解析 " + siteCode + " 站点下ASIN码为 " + asin + " 的评论信息,当前URL=" + page.getUrl());

        List<Review> reviewList = new ArrayList<Review>();
        for (Selectable reviewNode : reviewNodeList) {

            Review review = extractReviewItem(siteCode, asin, reviewNode);
            review.sarPageNum = UrlUtils.getValue(currentUrl, "pageNumber");
            reviewList.add(review);
        }

        mReviewService.addAll(reviewList);

        /* 当前URL没有pageNumber属性的话 */
        if (StringUtils.isEmpty(UrlUtils.getValue(currentUrl, "pageNumber"))) {

            List<String> pageUrlList = page.getHtml().xpath("//li[@class='page-button']/a/@href").all();

            List<Url> urlList = new ArrayList<Url>();

            if (CollectionUtils.isNotEmpty(pageUrlList)) {

                /* 说明是评论第一页，就根据最大页码拼装所有评论的页码 */
                String lastPageUrl = pageUrlList.get(pageUrlList.size() - 1);
                int totalPage = Integer.valueOf(UrlUtils.getValue(lastPageUrl, "pageNumber"));
                sLogger.info(asin + " 评论的最大页码为 " + totalPage);
                for (int i = 2; i <= totalPage; i++) {
                    Url url = new Url();
                    url.siteCode = siteCode;
                    url.saaAsin = asin;
                    url.parentUrl = currentUrl;
                    url.url = UrlUtils.setValue(currentUrl, "pageNumber", String.valueOf(i));
                    url.urlMD5 = UrlUtils.md5(url.url);

                    urlList.add(url);
                }
            }

            mUrlService.addAll(urlList);
        }
    }

    /**
     * 抽取评论列表根节点
     */
    List<Selectable> extractReviewNodeList(Page page) {
        return page.getHtml().xpath("//div[@class='a-section review']").nodes();
    }

    /**
     * 抽取单条评论
     */
    Review extractReviewItem(String siteCode, String asin, Selectable reviewNode) {
        Review review = new Review();
        int star = Integer.valueOf(reviewNode.xpath("//*[@data-hook='review-star-rating']/@class").regex(".*-([0-5]).*").get());//提取星级
        String title = reviewNode.xpath("//a[@data-hook='review-title']/text()").get();
        String reviewId = reviewNode.xpath("div/@id").get();
        String person = reviewNode.xpath("//a[@data-hook='review-author']/text()").get();
        String personID = reviewNode.xpath("//a[@data-hook='review-author']/@href").regex(".*profile/(.{13}).*").get();
        String time = reviewNode.xpath("//span[@data-hook='review-date']/text()").get();
        String version = reviewNode.xpath("//a[@data-hook='format-strip']/text()").get();
        String content = reviewNode.xpath("//span[@data-hook='review-body']/text()").get();
        String buyStatus = reviewNode.xpath("//span[@data-hook='avp-badge']/text()").get();

        review.basCode = siteCode;
        review.saaAsin = asin;
        review.sarStar = star;
        review.sarTitle = title;
        review.sarPersonId = personID;
        review.sarReviewId = reviewId;
        review.sarPerson = person;
        review.sarTime = time;
        review.sarDealTime = ReviewTimeUtil.parse(time, siteCode);
        review.sarVersion = version;
        review.sarContent = content;
        review.sarBuyStatus = buyStatus;
        sLogger.info(review);
        return review;
    }

    /**
     * 1，更新Url的爬取状态
     * 2，更新ASIN的爬取进度状态
     */
    public void updateUrlStatus(Page page) {
        sLogger.info("更新Url爬取状态...");
        super.updateUrlStatus(page);
        Url url = (Url) page.getRequest().getExtra(URL_EXTRA);
        mUrlService.updateAsinCrawledAll(url.saaAsin);
    }

    @Override
    public void execute() {
        sLogger.info("开始执行爬取任务...");
        List<Url> urlList = mUrlService.find(0);
        startToCrawl(urlList);
    }

    void startToCrawl(List<Url> urlList) {
        sLogger.info("找到状态码不为200的Url个数：" + urlList.size());
        if (CollectionUtils.isNotEmpty(urlList)) {

            Spider mSpider = Spider.create(this)
                    .setDownloader(sDownloader)
                    .thread(10);

            for (Url url : urlList) {
                Request request = new Request(url.url);
                request.putExtra(URL_EXTRA, url);
                mSpider.addRequest(request);

                url.sauCrawling = 1;
                mUrlService.update(url);
            }

            sLogger.info("开始爬取评论...");
            mSpider.start();
        }
    }

}
