package com.eccang.spider.amazon.monitor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchTop100;
import com.eccang.spider.amazon.pojo.relation.CustomerTop100;
import com.eccang.spider.amazon.service.batch.BatchTop100Service;
import com.eccang.spider.amazon.service.relation.CustomerTop100Service;
import com.eccang.spider.base.monitor.ScheduledTask;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 *          2017/2/10 10:18
 */
@Service
public class GenerateTop100BatchMonitor extends GenerateBatchMonitor implements ScheduledTask {

    private static Logger mLogger = LoggerFactory.getLogger(R.BusinessLog.TOP);
    @Autowired
    private BatchTop100Service mBatchTop100Service;
    @Autowired
    private CustomerTop100Service mCustomerTop100Service;

    @Override
    public void execute() {
        generateTop100Batch();
    }

    /**
     * 每个客户取一批满足条件的top100生成批次单号
     */
    private void generateTop100Batch() {
        Date currentTime = new Date();

        /*查询需要生成新的批次的客户关系关键词排名数据*/
        List<CustomerTop100> customerTop100s = mCustomerTop100Service.findNeedGenerateBatch();
        mLogger.info("需要生成新批次号的总量：" + customerTop100s.size());

        if (customerTop100s.size() == 0) {
            return;
        }

        /*生成总单并添加到数据库中*/
        String customerCode = null;
        if (CollectionUtils.isNotEmpty(customerTop100s)) {
            customerCode = customerTop100s.get(0).customerCode;
        }
        Batch batch = mBatchService.generate(customerCode, R.BatchType.TOP_100);
        mBatchService.add(batch);

        /*将批次单号与review建立关系*/
        List<BatchTop100> needAddList = new ArrayList<>();
        BatchTop100 batchTop100;
        for (CustomerTop100 customerTop100 : customerTop100s) {

            batchTop100 = new BatchTop100();
            batchTop100.batchNum = batch.number;
            batchTop100.siteCode = customerTop100.siteCode;
            batchTop100.type = R.BatchType.TOP_100;

            needAddList.add(batchTop100);

            customerTop100.syncTime = currentTime;
            mCustomerTop100Service.update(customerTop100);
        }

        mLogger.info("客户 " + customerCode + " 生成的批次量为：" + needAddList.size());

        /*添加创建详单信息*/
        mBatchTop100Service.addAll(needAddList);

        mLogger.info("生成Top100商品信息爬取批次：成功");
    }
}