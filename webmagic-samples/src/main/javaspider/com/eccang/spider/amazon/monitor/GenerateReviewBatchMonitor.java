package com.eccang.spider.amazon.monitor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchReview;
import com.eccang.spider.amazon.pojo.relation.CustomerReview;
import com.eccang.spider.amazon.service.batch.BatchReviewService;
import com.eccang.spider.amazon.service.relation.CustomerReviewService;
import com.eccang.spider.base.monitor.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jervis
 * @version V0.2
 * @Description: Review批次生成监控
 * @date 2016/12/26 17:13
 */
@Service
public class GenerateReviewBatchMonitor extends GenerateBatchMonitor implements ScheduledTask {

    private static Logger mLogger = LoggerFactory.getLogger(R.BusinessLog.MS);
    @Autowired
    private BatchReviewService mBatchReviewService;
    @Autowired
    private CustomerReviewService mCustomerReviewService;

    @Override
    public void execute() {

        /*查询已经完成的客户关系review数据*/
        List<CustomerReview> customerReviewList = mCustomerReviewService.findNeedGenerateBatch();

        generate(customerReviewList, false);
    }

    public void generate(List<CustomerReview> customerReviewList, boolean immediate) {
        Date currentTime = new Date();

        mLogger.info("需要生成新批次号的总量：{}", customerReviewList.size());

        /*按客户码分组*/
        Map<String, List<CustomerReview>> customerListMap = initCustomerListMap(customerReviewList);

        /*生成总单与详单*/
        for (String customerCode : customerListMap.keySet()) {

            List<CustomerReview> rmList = customerListMap.get(customerCode);
            /*生成总单并添加到数据库中*/
            Batch batch = mBatchService.generate(customerCode, R.BatchType.REVIEW_MONITOR);
            batch.immediate = immediate ? 1 : 0;
            mBatchService.add(batch);

            /*将批次单号与review建立关系*/
            List<BatchReview> needAddList = new ArrayList<>();
            for (CustomerReview customerReview : rmList) {
                BatchReview batchReview = new BatchReview();
                batchReview.reviewID = customerReview.reviewId;
                batchReview.batchNumber = batch.number;
                batchReview.siteCode = customerReview.siteCode;
                batchReview.type = R.CrawlType.REVIEW_MONITOR;
                needAddList.add(batchReview);

                if (!immediate) {
                    customerReview.finishTime = currentTime;
                    mCustomerReviewService.update(customerReview);
                }
            }
            mLogger.info("客户 {} 生成的批次量为：{}", customerCode, needAddList.size());
            /*添加创建详单信息*/
            mBatchReviewService.addAll(needAddList);

            mLogger.info("生成Review监控批次：成功");
        }
    }
}
