package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchAsin;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerFollowSell;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerFollowSellService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Override
    public void execute() {
        Date currentTime = new Date();

        /*查询已经完成的客户关系review数据*/
        List<CustomerFollowSell> customerFollowSellList = mCustomerFollowSellService.findNeedGenerateBatch();
        /* 去掉已经下架的记录，不需要更新爬取 */
        for (CustomerFollowSell customerFollowSell : customerFollowSellList) {
            if (mNoSellService.isExist(new Asin(customerFollowSell.siteCode, customerFollowSell.asin))) {
                customerFollowSellList.remove(customerFollowSell);
            }
            customerFollowSell.syncTime = currentTime;
            mCustomerFollowSellService.update(customerFollowSell);
        }

        mLogger.info("需要生成新批次号的总量：" + customerFollowSellList.size());

        /*按客户码分组*/
        Map<String, List<CustomerFollowSell>> customerListMap = initCustomerListMap(customerFollowSellList);

        /*生成总单与详单*/
        for (String customerCode : customerListMap.keySet()) {

            List<CustomerFollowSell> rmList = customerListMap.get(customerCode);

            /*将批次单号与Asin建立关系*/
            List<BatchAsin> needAddList = new ArrayList<>();

            for (CustomerFollowSell customerAsin : rmList) {


                /*BatchAsin batchAsin = new BatchAsin();
                batchAsin.siteCode = customerAsin.siteCode;
                batchAsin.asin = customerAsin.asin;
                batchAsin.priority = customerAsin.priority;
                batchAsin.star = customerAsin.star;
                batchAsin.type = 2;
                batchAsin.status = 4;
                batchAsin.crawled = 1;
                needAddList.add(batchAsin);*/
            }

            mLogger.info("客户 " + customerCode + " 生成的批次量为：" + needAddList.size());
            /*添加创建详单信息*/
            mBatchService.addBatch(customerCode, needAddList, 2, 0);

            mLogger.info("生成Review更新爬取批次：成功");
        }
    }
}
