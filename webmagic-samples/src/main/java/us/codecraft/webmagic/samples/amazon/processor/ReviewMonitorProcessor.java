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
    private UrlService mUrlService;
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
            mReviewService.add(review);

            /* 二期业务 */
            /* 更新详单 */
            List<BatchReview> batchReviewList = mBatchReviewService.findByReviewID(reviewId);
            for (BatchReview batchReview : batchReviewList) {
                batchReview.times++;
                batchReview.status = 2;

                /*更新关系表下的review完成时间*/
                Batch batch = mBatchService.findByBatchNumber(batchReview.batchNumber);
                CustomerReview customerReview = mCustomerReviewService.findCustomerReview(batch.customerCode, batchReview.reviewID);
                customerReview.finishTime = new Date();
                mCustomerReviewService.updateByReviewIdCustomerCode(customerReview);
            }
            mBatchReviewService.updateAll(batchReviewList);

            /* 更新总单 */
            for (BatchReview batchReview : batchReviewList) {
                Batch batch = mBatchService.findByBatchNumber(batchReview.batchNumber);
                batch.times++;
                if (batch.startTime == null) {
                    batch.startTime = new Date();
                }
                mBatchService.update(batch);
            }

        }
    }

    @Override
    protected boolean needUpdateStatus() {
        /* 更新成200状态后，还怎么继续监听呢，所以不需要更新状态码，只累计爬取次数
         * 根据次数和优先级取当前需要爬取哪些监听的URL，缓解一次获取所有URL的程序爬
         * 取压力。 */
        return false;
    }

    @Override
    public void execute() {
        List<Url> urlList = mUrlService.findMonitorUrlList();
        sLogger.info("开始爬取被监听的Review...数量为" + urlList.size());
        startToCrawl(urlList);
    }

}