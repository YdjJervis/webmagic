package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.BatchReview;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewMonitor;
import us.codecraft.webmagic.samples.amazon.service.BatchReviewService;
import us.codecraft.webmagic.samples.amazon.service.BatchService;
import us.codecraft.webmagic.samples.amazon.service.ReviewMonitorService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

import java.util.*;

/**
 * @author Hardy
 * @version V0.2
 *          自动检测review并生成批次
 *          2016/12/7 10:10
 */
@Service
public class GenerateBatchMonitor implements ScheduledTask {

    @Autowired
    ReviewMonitorService mReviewMonitorService;
    @Autowired
    BatchService mBatchService;
    @Autowired
    BatchReviewService mBatchReviewService;

    @Override
    public void execute() {
        generateReviewMonitorBatch();
    }

    /**
     * 自动生成新的批次
     */
    public void generateReviewMonitorBatch() {
        /*通过客户码分组*/
        Map<String, List<ReviewMonitor>> reviewMonitorGroupByCustomerMap = new HashMap<String, List<ReviewMonitor>>();
        /*查询已经完成的客户关系review数据*/
        List<ReviewMonitor> reviewMonitorList = mReviewMonitorService.findHasFinished();
        /*需要生成批次的数据集合*/
        List<ReviewMonitor> need2GenerateBatchList = new ArrayList<ReviewMonitor>();
        /*需要生成批次的关系数据中的所有客户码*/
        List<String> customerCodesList = new ArrayList<String>();

        /*筛选出需要生成批次的数据*/
        for (ReviewMonitor reviewMonitor : reviewMonitorList) {
            /*计算上一次完成时间与当前时间间隔*/
            Long sleepTime = (new Date().getTime() - reviewMonitor.finishTime.getTime()) / (1000 * 60 * 60);
            if (reviewMonitor.status == 2 && sleepTime > reviewMonitor.frequency) {
                need2GenerateBatchList.add(reviewMonitor);
                /*统计需要生成新的批次号的客户码*/
                if (!customerCodesList.contains(reviewMonitor.customerCode)) {
                    customerCodesList.add(reviewMonitor.customerCode);
                }
            }
        }

        if (customerCodesList.size() > 0) {
            for (String customerCode : customerCodesList) {
                reviewMonitorGroupByCustomerMap.put(customerCode, null);
            }
        }

        /*按客户码分组*/
        List<ReviewMonitor> reviewMonitorValue;
        for (ReviewMonitor reviewMonitor : need2GenerateBatchList) {
            if(reviewMonitorGroupByCustomerMap.get(reviewMonitor.customerCode) == null) {
                reviewMonitorValue = new ArrayList<ReviewMonitor>();
                reviewMonitorValue.add(reviewMonitor);
                reviewMonitorGroupByCustomerMap.put(reviewMonitor.customerCode, reviewMonitorValue);
            } else {
                reviewMonitorGroupByCustomerMap.get(reviewMonitor.customerCode).add(reviewMonitor);
            }
        }

        /*生成总单与详单*/
        List<BatchReview> needAddList;
        for (Map.Entry<String, List<ReviewMonitor>> entry : reviewMonitorGroupByCustomerMap.entrySet()) {
            String customerCode = entry.getKey();
            List<ReviewMonitor> rmList = entry.getValue();
            /*生成总单并添加到数据库中*/
            Batch batch = mBatchService.generate(customerCode, 1);
            mBatchService.add(batch);

            /*将批次单号与review建立关系*/
            needAddList = new ArrayList<BatchReview>();
            BatchReview batchReview;
            for (ReviewMonitor reviewMonitor : rmList) {
                batchReview = new BatchReview();
                batchReview.reviewID = reviewMonitor.smrReviewId;
                batchReview.batchNumber = batch.number;
                batchReview.siteCode = reviewMonitor.siteCode;
                needAddList.add(batchReview);
            }

            /*添加创建详单信息*/
            if (needAddList.size() > 0) {
                mBatchReviewService.addAll(needAddList);
            }
        }

    }
}