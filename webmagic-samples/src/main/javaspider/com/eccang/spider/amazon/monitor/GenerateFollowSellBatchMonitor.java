package com.eccang.spider.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Asin;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchFollowSell;
import com.eccang.spider.amazon.pojo.relation.CustomerFollowSell;
import com.eccang.spider.amazon.service.batch.BatchFollowSellService;
import com.eccang.spider.amazon.service.relation.CustomerFollowSellService;
import com.eccang.spider.base.monitor.ScheduledTask;

import java.util.*;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 批次批次生成监控
 * @date 2016/12/26 17:13
 */
@Service
public class GenerateFollowSellBatchMonitor extends GenerateBatchMonitor implements ScheduledTask {

    @Autowired
    private CustomerFollowSellService mCustomerFollowSellService;
    @Autowired
    private BatchFollowSellService mBatchFollowSellService;

    @Override
    public void execute() {

        /*查询已经完成的客户关系review数据*/
        List<CustomerFollowSell> customerFollowSellList = mCustomerFollowSellService.findNeedGenerateBatch();
        generate(customerFollowSellList, false);
    }

    public void generate(List<CustomerFollowSell> customerFollowSellList, boolean immediate) {
        Date currentTime = new Date();

        /* 去掉已经下架的记录，不需要更新爬取 */
        ListIterator<CustomerFollowSell> crsIterator = customerFollowSellList.listIterator();
        while (crsIterator.hasNext()) {
            CustomerFollowSell cfs = crsIterator.next();
            if (mNoSellService.isExist(new Asin(cfs.siteCode, cfs.asin))) {
                crsIterator.remove();
            }

            if (!immediate) {
                cfs.syncTime = currentTime;
                mCustomerFollowSellService.update(cfs);
            }
        }

        mLogger.info("需要生成新批次号的总量：" + customerFollowSellList.size());

        /*按客户码分组*/
        Map<String, List<CustomerFollowSell>> customerListMap = initCustomerListMap(customerFollowSellList);

        /*生成总单与详单*/
        for (String customerCode : customerListMap.keySet()) {

            List<CustomerFollowSell> cfsList = customerListMap.get(customerCode);

            /*生成总单并添加到数据库中*/
            Batch batch = mBatchService.generate(customerCode, R.BatchType.FOLLOW_SELL);
            batch.immediate = immediate ? 1 : 0;
            mBatchService.add(batch);

            /*将批次单号与Asin建立关系*/
            List<BatchFollowSell> needAddList = new ArrayList<>();

            BatchFollowSell batchFollowSell;
            for (CustomerFollowSell customerFollowSell : cfsList) {

                batchFollowSell = new BatchFollowSell();
                batchFollowSell.batchNumber = batch.number;
                batchFollowSell.siteCode = customerFollowSell.siteCode;
                batchFollowSell.asin = customerFollowSell.asin;
                batchFollowSell.type = R.CrawlType.FOLLOW_SELL;
                batchFollowSell.priority = customerFollowSell.priority;

                needAddList.add(batchFollowSell);
            }

            mLogger.info("客户 " + customerCode + " 生成的批次量为：" + needAddList.size());
            mBatchFollowSellService.addAll(needAddList);
            mLogger.info("生成Review更新爬取批次：成功");
        }
    }
}
