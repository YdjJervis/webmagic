package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.pojo.batch.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchReview;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerReview;
import us.codecraft.webmagic.samples.amazon.service.ReviewService;
import us.codecraft.webmagic.samples.amazon.service.batch.BatchReviewService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerReviewService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

import java.util.Date;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论监听表监控的抓取
 * @date 2016/10/12 15:05
 */
@Service
public class ReviewMonitorProcessor extends BasePageProcessor implements ScheduledTask {

    @Autowired
    private ReviewService mReviewService;
    @Autowired
    private BatchReviewService mBatchReviewService;
    @Autowired
    private CustomerReviewService mCustomerReviewService;

    @Override
    protected void dealOtherPage(Page page) {
        if (page.getUrl().get().contains("customer-reviews")) {

            String reviewId = page.getUrl().regex(".*customer-reviews/(.*)").get();
            Review review = mReviewService.findByReviewId(reviewId);

            String star = page.getHtml().xpath("//tbody//div[@style='margin-bottom:0.5em;']//img").regex(".*stars-([1-5]).*").get();

            if (!NumberUtils.isNumber(star)) {
                sLogger.warn("抱歉，商品已经下架，没有成功解析颗星数：" + star);
                updateBatchStatus(page, false, false);
            } else {
                int stars = Integer.valueOf(star);
                String title = page.getHtml().xpath("//tbody//div[@style='margin-bottom:0.5em;']/b/text()").all().get(0);
                String content = page.getHtml().xpath("//tbody//div[@class='reviewText']/text()").get();

                boolean changed = false;
                if (review.star != stars || !review.title.equals(title) || review.content.equals(content)) {
                    changed = true;
                }

                review.star = stars;
                review.title = title;
                review.content = content;

                sLogger.info(review);
                mReviewService.update(review);

                updateBatchStatus(page, true, changed);
            }

        }
    }

    /**
     * 更新：
     * 1，批次总单；
     * 2，批次详单；
     * 3，客户Review关系。
     */
    private void updateBatchStatus(Page page, boolean isOnSell, boolean changed) {
        String reviewId = page.getUrl().regex(".*customer-reviews/(.*)").get();
        /* 二期业务 */
        /* 更新详单 */
        BatchReview batchReview = mBatchReviewService.findByReviewID(getUrl(page).batchNum, reviewId);
        batchReview.status = 2;
        if (changed) {
            batchReview.isChanged = 1;
        }
        mBatchReviewService.update(batchReview);

        /* 更新批次总单的状态 */
        Batch batch = mBatchService.findByBatchNumber(batchReview.batchNumber);

        Date currentTime = new Date();
        if (batch.startTime == null) {
            batch.startTime = currentTime;
        }
        float progress = mBatchReviewService.findAverageProgress(batchReview.batchNumber);
        batch.progress = progress;
        if (progress == 1) {
            batch.finishTime = currentTime;
            batch.status = 2;

            /* 监控批次完成，把批次放进推送队列 */
            mPushQueueService.add(batch.number);
        } else {
            batch.status = 1;
        }
        mBatchService.update(batch);

        /* 更新客户-Review关系记录状态 */
        CustomerReview customerReview = mCustomerReviewService.findCustomerReview(batch.customerCode, reviewId);
        customerReview.times++;
        customerReview.finishTime = currentTime;
        if (!isOnSell) {
            customerReview.onSell = 0;
        }
        if (changed) {
            customerReview.crawl = 0;
        }
        mCustomerReviewService.update(customerReview);

        archiveCurrentUrl(page);
    }

    @Override
    void dealPageNotFound(Page page) {
        updateBatchStatus(page, false, false);
    }

    @Override
    public void execute() {
        List<Url> urlList = mUrlService.findMonitorUrlList();
        sLogger.info("开始爬取被监听的Review...数量为" + urlList.size());
        startToCrawl(urlList);
    }

}