package com.eccang.spider.amazon.monitor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Asin;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.pojo.dict.Customer;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;
import com.eccang.spider.base.monitor.ScheduledTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Jervis
 * @version V0.2
 * @Description: Asin批次生成监控
 * @date 2016/12/26 17:13
 */
@Service
public class GenerateAsinBatchMonitor extends GenerateBatchMonitor implements ScheduledTask {

    private final static Logger mLogger = LoggerFactory.getLogger(R.BusinessLog.AS);

    @Autowired
    private CustomerAsinService mCustomerAsinService;

    @Override
    public void execute() {

        mLogger.info("开始生成批次...");

        Date currentTime = new Date();

        /*查询已经完成的客户关系review数据*/
        List<CustomerAsin> customerAsinList = mCustomerAsinService.findNeedGenerateBatch();
        mLogger.info("查询到的需要生成批次的总量：" + customerAsinList.size());

        /* 去掉已经下架的记录，不需要更新爬取 */
        ListIterator<CustomerAsin> caIterator = customerAsinList.listIterator();
        while (caIterator.hasNext()) {
            CustomerAsin customerAsin = caIterator.next();
            if (mNoSellService.isExist(new Asin(customerAsin.siteCode, customerAsin.asin))) {
                caIterator.remove();
            }
            customerAsin.syncTime = currentTime;
            mCustomerAsinService.update(customerAsin);
        }

        /*按客户码分组*/
        Map<String, List<CustomerAsin>> customerListMap = initCustomerListMap(customerAsinList);

        /*生成总单与详单*/
        for (String customerCode : customerListMap.keySet()) {
            Customer customer = mCustomerService.findByCode(customerCode);
            mLogger.info("客户" + customerCode + "开始生成批次...");

            List<CustomerAsin> rmList = customerListMap.get(customerCode);
            if (customer.debug) {
                mLogger.debug(rmList.toString());
            }

            /*将批次单号与Asin建立关系*/
            List<BatchAsin> needAddList = new ArrayList<>();

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

            /*添加创建详单信息*/
            Batch batch = mBatchService.addBatch(customerCode, needAddList, R.BatchType.REVIEW_UPDATE, 0, 0);
            if (customer.debug) {
                mLogger.debug(batch.toString());
                mLogger.debug(needAddList.toString());
            }

            mLogger.info("客户 " + customerCode + " 生成的批次单号/数量:" + batch.number + "/" + needAddList.size());

            mLogger.info("客户 " + customerCode + "结束生成批次。");
        }

        mLogger.info("结束生成批次。");
    }
}
