package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.BatchReview;
import us.codecraft.webmagic.samples.amazon.pojo.CustomerReview;
import us.codecraft.webmagic.samples.amazon.service.BatchReviewService;
import us.codecraft.webmagic.samples.amazon.service.BatchService;
import us.codecraft.webmagic.samples.amazon.service.CustomerReviewService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

import java.util.*;

/**
 * @author Hardy
 * @version V0.2
 * @Description 自动检测review并生成批次
 * @date 2016/12/7 10:10
 */
@Service
public class BatchGenerateMonitor implements ScheduledTask {

    @Autowired
    CustomerReviewService mCustomerReviewService;
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
        Map<String, List<CustomerReview>> customerListMap = new HashMap<String, List<CustomerReview>>();

        /*查询已经完成的客户关系review数据*/
        List<CustomerReview> customerReviewList = mCustomerReviewService.findNeedGenerateBatch();

        /*按客户码分组*/
        for (CustomerReview customerReview : customerReviewList) {
            if (customerListMap.get(customerReview.customerCode) == null) {
                List<CustomerReview> list = new ArrayList<CustomerReview>();
                list.add(customerReview);
                customerListMap.put(customerReview.customerCode, list);
            } else {
                customerListMap.get(customerReview.customerCode).add(customerReview);
            }
        }

        /*生成总单与详单*/
        List<BatchReview> needAddList;
        for (String customerCode : customerListMap.keySet()) {

            List<CustomerReview> rmList = customerListMap.get(customerCode);
            /*生成总单并添加到数据库中*/
            Batch batch = mBatchService.generate(customerCode, 1);
            mBatchService.add(batch);

            /*将批次单号与review建立关系*/
            needAddList = new ArrayList<BatchReview>();
            BatchReview batchReview;
            for (CustomerReview customerReview : rmList) {
                batchReview = new BatchReview();
                batchReview.reviewID = customerReview.reviewId;
                batchReview.batchNumber = batch.number;
                batchReview.siteCode = customerReview.siteCode;
                needAddList.add(batchReview);

                /*将重新生成批次的customer-review关系表的数据爬取状态更新为未爬取*/
                customerReview.status = 0;
                mCustomerReviewService.updateByReviewIdCustomerCode(customerReview);
            }

            /*添加创建详单信息*/
            mBatchReviewService.addAll(needAddList);
        }

    }
}