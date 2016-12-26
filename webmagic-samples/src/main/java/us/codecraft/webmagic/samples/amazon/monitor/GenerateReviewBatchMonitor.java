package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.batch.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchReview;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerReview;
import us.codecraft.webmagic.samples.amazon.service.batch.BatchReviewService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerReviewService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

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

    @Autowired
    private BatchReviewService mBatchReviewService;
    @Autowired
    private CustomerReviewService mCustomerReviewService;

    @Override
    public void execute() {
        Date currentTime = new Date();
        /*查询已经完成的客户关系review数据*/
        List<CustomerReview> customerReviewList = mCustomerReviewService.findNeedGenerateBatch();
        mLogger.info("需要生成新批次号的总量：" + customerReviewList.size());

        /*按客户码分组*/
        Map<String, List<CustomerReview>> customerListMap = initCustomerListMap(customerReviewList);

        /*生成总单与详单*/
        for (String customerCode : customerListMap.keySet()) {

            List<CustomerReview> rmList = customerListMap.get(customerCode);
            /*生成总单并添加到数据库中*/
            Batch batch = mBatchService.generate(customerCode, 1);
            mBatchService.add(batch);

            /*将批次单号与review建立关系*/
            List<BatchReview> needAddList = new ArrayList<BatchReview>();
            for (CustomerReview customerReview : rmList) {
                BatchReview batchReview = new BatchReview();
                batchReview.reviewID = customerReview.reviewId;
                batchReview.batchNumber = batch.number;
                batchReview.siteCode = customerReview.siteCode;
                batchReview.type = 4;
                needAddList.add(batchReview);

                customerReview.finishTime = currentTime;
                mCustomerReviewService.update(customerReview);
            }
            mLogger.info("客户 " + customerCode + " 生成的批次量为：" + needAddList.size());
            /*添加创建详单信息*/
            mBatchReviewService.addAll(needAddList);

            mLogger.info("生成Review监控批次：成功");
        }
    }
}
