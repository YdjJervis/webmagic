package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.*;
import us.codecraft.webmagic.samples.amazon.service.*;
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
    private UrlHistoryService mUrlHistoryService;
    @Autowired
    private ReviewService mReviewService;

    @Autowired
    private BatchReviewService mBatchReviewService;
    @Autowired
    private BatchService mBatchService;
    @Autowired
    private CustomerReviewService mCustomerReviewService;

    @Override
    protected void dealOtherPage(Page page) {
        if (page.getUrl().get().contains("customer-reviews")) {

            String reviewId = page.getUrl().regex(".*customer-reviews/(.*)").get();
            Review review = mReviewService.findByReviewId(reviewId);

            String star = page.getHtml().xpath("//tbody//div[@style='margin-bottom:0.5em;']//img").regex(".*stars-([1-5]).*").get();
            if (!NumberUtils.isNumber(star)) {
                sLogger.error("抱歉，没有成功解析颗星数：" + star);
                return;
            }
            review.star = Integer.valueOf(star);
            review.title = page.getHtml().xpath("//tbody//div[@style='margin-bottom:0.5em;']/b/text()").all().get(0);
            review.time = page.getHtml().xpath("//tbody//div[@style='margin-bottom:0.5em;']/nobr/text()").get();
            review.content = page.getHtml().xpath("//tbody//div[@class='reviewText']/text()").get();

            sLogger.info(review);
            mReviewService.update(review);

            updateBatchStatus(page);
        }
    }

    private void updateBatchStatus(Page page) {
        String reviewId = page.getUrl().regex(".*customer-reviews/(.*)").get();
        /* 二期业务 */
        /* 更新详单 */
        BatchReview batchReview = mBatchReviewService.findByReviewID(getUrl(page).batchNum, reviewId);
        batchReview.status = 2;
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
        } else {
            batch.status = 1;
        }
        mBatchService.update(batch);

        /* 跟新客户-Review关系记录状态 */
        CustomerReview customerReview = mCustomerReviewService.findCustomerReview(batch.customerCode, reviewId);
        customerReview.times++;
        customerReview.finishTime = currentTime;
        mCustomerReviewService.update(customerReview);

        /* URL归档到历史表 */
        mUrlService.deleteByUrlMd5(getUrl(page).urlMD5);
        mUrlHistoryService.add(getUrl(page));
    }

    @Override
    void dealPageNotFound(Page page) {
        updateBatchStatus(page);
    }

    @Override
    public void execute() {
        List<Url> urlList = mUrlService.findMonitorUrlList();
        sLogger.info("开始爬取被监听的Review...数量为" + urlList.size());
        startToCrawl(urlList);
    }

}