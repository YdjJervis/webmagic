package com.eccang.spider.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Asin;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;
import com.eccang.spider.base.monitor.ScheduledTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jervis
 * @version V0.2
 * @Description: Asin批次生成监控
 * @date 2016/12/26 17:13
 */
@Service
public class GenerateAsinBatchMonitor extends GenerateBatchMonitor implements ScheduledTask {

    @Autowired
    private CustomerAsinService mCustomerAsinService;

    @Override
    public void execute() {
        Date currentTime = new Date();

        /*查询已经完成的客户关系review数据*/
        List<CustomerAsin> customerAsinList = mCustomerAsinService.findNeedGenerateBatch();
        /* 去掉已经下架的记录，不需要更新爬取 */
        for (CustomerAsin customerAsin : customerAsinList) {
            if (mNoSellService.isExist(new Asin(customerAsin.siteCode, customerAsin.asin))) {
                customerAsinList.remove(customerAsin);
            }
            customerAsin.syncTime = currentTime;
            mCustomerAsinService.update(customerAsin);
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
                batchAsin.priority = customerAsin.priority;
                batchAsin.star = customerAsin.star;
                batchAsin.type = R.BatchType.REVIEW_UPDATE;
                batchAsin.status = 4;
                batchAsin.crawled = 1;
                needAddList.add(batchAsin);
            }

            mLogger.info("客户 " + customerCode + " 生成的批次量为：" + needAddList.size());
            /*添加创建详单信息*/
            mBatchService.addBatch(customerCode, needAddList, R.BatchType.REVIEW_UPDATE, 0);

            mLogger.info("生成Review更新爬取批次：成功");
        }
    }
}
