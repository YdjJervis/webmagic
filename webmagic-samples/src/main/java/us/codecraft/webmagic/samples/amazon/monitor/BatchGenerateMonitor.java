package us.codecraft.webmagic.samples.amazon.monitor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.*;
import us.codecraft.webmagic.samples.amazon.service.*;
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

    private Logger mLogger = Logger.getLogger(getClass());
    @Autowired
    private CustomerAsinService mCustomerAsinService;
    @Autowired
    private CustomerReviewService mCustomerReviewService;
    @Autowired
    private BatchService mBatchService;
    @Autowired
    private BatchReviewService mBatchReviewService;
    @Autowired
    private NoSellService mNoSellService;

    @Override
    public void execute() {
        generateReviewMonitorBatch();
        generateReviewUpdateBatch();
    }

    /**
     * 每个客户取一批满足条件的Review生成批次单号
     */
    public void generateReviewMonitorBatch() {

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
                needAddList.add(batchReview);

                /*将重新生成批次的customer-review关系表的数据爬取状态更新为未爬取*/
                customerReview.status = 0;
                mCustomerReviewService.updateByReviewIdCustomerCode(customerReview);
            }
            mLogger.info("客户 " + customerCode + " 生成的批次量为：" + needAddList.size());
            /*添加创建详单信息*/
            mBatchReviewService.addAll(needAddList);

            mLogger.info("生成Review监控批次：成功");
        }

    }

    /**
     * 每个客户取一批满足条件的Asin生成批次单号
     */
    public void generateReviewUpdateBatch() {

        /*查询已经完成的客户关系review数据*/
        List<CustomerAsin> customerAsinList = mCustomerAsinService.findNeedGenerateBatch();
        /* 去掉已经下架的记录，不需要更新爬取 */
        for (CustomerAsin customerAsin : customerAsinList) {
            if (mNoSellService.isExist(new Asin(customerAsin.siteCode, customerAsin.asin))) {
                customerAsinList.remove(customerAsin);
            }
        }

        mLogger.info("需要生成新批次号的总量：" + customerAsinList.size());

        /*按客户码分组*/
        Map<String, List<CustomerAsin>> customerListMap = initCustomerListMap(customerAsinList);

        /*生成总单与详单*/
        for (String customerCode : customerListMap.keySet()) {

            List<CustomerAsin> rmList = customerListMap.get(customerCode);

            /*将批次单号与Asin建立关系*/
            List<BatchAsin> needAddList = new ArrayList<BatchAsin>();

            for (CustomerAsin customerAsin : rmList) {

                BatchAsin batchAsin = new BatchAsin();
                batchAsin.siteCode = customerAsin.siteCode;
                batchAsin.asin = customerAsin.asin;
                batchAsin.star = customerAsin.star;
                batchAsin.type = 2;
                batchAsin.status = 4;
                batchAsin.crawled = 1;
                needAddList.add(batchAsin);

            }

            mLogger.info("客户 " + customerCode + " 生成的批次量为：" + needAddList.size());
            /*添加创建详单信息*/
            mBatchService.addBatch(customerCode, needAddList, 2);

            mLogger.info("生成Review更新爬取批次：成功");
        }
    }

    private <T> Map<String, List<T>> initCustomerListMap(List<T> srcList) {
        Map<String, List<T>> customerListMap = new HashMap<String, List<T>>();

        for (T item : srcList) {
            String customerCode = "";
            if (item instanceof CustomerReview) {
                customerCode = ((CustomerReview) item).customerCode;
            } else if (item instanceof CustomerAsin) {
                customerCode = ((CustomerAsin) item).customerCode;
            }

            if (customerListMap.get(customerCode) == null) {
                customerListMap.put(customerCode, new ArrayList<T>());
            }

            customerListMap.get(customerCode).add(item);
        }
        return customerListMap;
    }
}