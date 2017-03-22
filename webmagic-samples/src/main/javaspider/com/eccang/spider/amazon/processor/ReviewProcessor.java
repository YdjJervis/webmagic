package com.eccang.spider.amazon.processor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.crawl.Review;
import com.eccang.spider.amazon.service.crawl.ReviewService;
import com.eccang.spider.amazon.service.relation.AsinRootAsinService;
import com.eccang.spider.amazon.util.ReviewTimeUtil;
import com.eccang.spider.base.monitor.ScheduledTask;
import com.eccang.spider.base.util.UrlUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
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

    private final static Logger mLogger = LoggerFactory.getLogger(R.BusinessLog.AS);
    @Autowired
    protected ReviewService mReviewService;

    @Autowired
    private AsinRootAsinService mAsinRootAsinService;

    @Override
    protected void dealOtherPage(Page page) {
        mLogger.info("开始处理页面回调...");
        String currentUrl = page.getUrl().get();
        Url urlExtra = getUrl(page);

        List<Selectable> reviewNodeList = extractReviewNodeList(page);
        mLogger.info("抽取Review条数：" + reviewNodeList.size());

        String asin = extractAsin(page);
        String siteCode = extractSite(page).code;

        mLogger.info("解析 " + siteCode + " 站点下ASIN:" + asin);

        List<Review> reviewList = new ArrayList<>();
        for (Selectable reviewNode : reviewNodeList) {

            Review review = extractReviewItem(siteCode, reviewNode);
            review.rootAsin = mAsinRootAsinService.findByAsin(asin, siteCode).rootAsin;
            String pageNum = UrlUtils.getValue(currentUrl, "pageNumber");
            review.pageNum = StringUtils.isEmpty(pageNum) ? 1 : Integer.valueOf(pageNum);
            if (sProfile.debug) {
                mLogger.debug("抽取的Review：" + review);
            }

            reviewList.add(review);
        }

        try {
            mReviewService.addAll(reviewList);
        } catch (Exception e) {
            mLogger.error("保存Review列表失败：" + reviewList);
        }

        /* 当前URL没有pageNumber属性的话 */
        if (StringUtils.isEmpty(UrlUtils.getValue(currentUrl, "pageNumber"))) {

            List<Url> urlList = new ArrayList<>();

            int totalPage = extractTotalPage(page);
            mLogger.info(asin + " 评论的最大页码为 " + totalPage);
            for (int i = 2; i <= totalPage; i++) {
                Url url = new Url();
                url.batchNum = urlExtra.batchNum;
                url.type = R.CrawlType.REVIEW_ALL;
                url.siteCode = siteCode;
                url.asin = asin;
                url.parentUrl = currentUrl;
                url.url = UrlUtils.setValue(currentUrl, "pageNumber", String.valueOf(i));
                url.urlMD5 = UrlUtils.md5(url.batchNum + url.url);

                if (sProfile.debug) {
                    mLogger.debug("生成Reivew翻页Url：" + url);
                }
                urlList.add(url);
            }

            mUrlService.addAll(urlList);
        }

        upgradeCrawlStatus(page);
        mLogger.info("结束处理页面回调。");
    }

    /**
     * @return 抽取的最大页码
     */
    int extractTotalPage(Page page) {
        List<String> pageUrlList = page.getHtml().xpath("//li[@class='page-button']/a/@href").all();
        if (pageUrlList.size() == 0) {
            return 1;
        }

        /* 说明是评论第一页，就根据最大页码拼装所有评论的页码 */
        String lastPageUrl = pageUrlList.get(pageUrlList.size() - 1);
        return Integer.valueOf(UrlUtils.getValue(lastPageUrl, "pageNumber"));
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
    Review extractReviewItem(String siteCode, Selectable reviewNode) {
        Review review = new Review();
        int star = Integer.valueOf(reviewNode.xpath("//*[@data-hook='review-star-rating']/@class").regex(".*-([0-5]).*").get());//提取星级
        String title = reviewNode.xpath("//a[@data-hook='review-title']/text()").get();
        String reviewId = reviewNode.xpath("div/@id").get();
        String person = reviewNode.xpath("//a[@data-hook='review-author']/text()").get();
        String personID = reviewNode.xpath("//a[@data-hook='review-author']/@href").regex("profile/([0-9a-zA-Z]*)").get();
        String time = reviewNode.xpath("//span[@data-hook='review-date']/text()").get();
        String version = reviewNode.xpath("//a[@data-hook='format-strip']/text()").get();
        String content = reviewNode.xpath("//span[@data-hook='review-body']/text()").get();
        String votes = reviewNode.xpath("//span[@data-hook='review-voting-widget']//span[@class='review-votes']/text()").get();
        String buyStatus = reviewNode.xpath("//span[@data-hook='avp-badge']/text()").get();

        if (StringUtils.isNotEmpty(votes)) {
            votes = votes.trim();
        }

        review.siteCode = siteCode;
        review.star = star;
        review.title = title;
        review.personId = personID;
        review.reviewId = reviewId;
        review.person = person;
        review.time = time;
        review.dealTime = ReviewTimeUtil.parse(time, siteCode);
        review.version = version;
        review.content = content;
        review.votes = votes;
        review.buyStatus = buyStatus;
        return review;
    }

    /**
     * 如果ASIN爬取结束
     * 1，更新URL列表，历史URL列表
     * 2，更新ASIN表状态
     */
    private void upgradeCrawlStatus(Page page) {
        mLogger.info("更新Url爬取状态...");
        Url url = (Url) page.getRequest().getExtra(URL_EXTRA);
        mUrlService.updateAsinCrawledAll(url);
    }

    @Override
    public void execute() {
        mLogger.info("开始执行Review爬取任务...");
        List<Url> urlList = mUrlService.find(R.CrawlType.REVIEW_ALL);
        startToCrawl(urlList);
    }
}
