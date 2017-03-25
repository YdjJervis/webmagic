package com.eccang.spider.amazon.monitor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchRank;
import com.eccang.spider.amazon.pojo.relation.CustomerKeywordRank;
import com.eccang.spider.amazon.service.batch.BatchRankService;
import com.eccang.spider.amazon.service.relation.CustomerKeywordRankService;
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
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/26 18:28
 */
@Service
public class GenerateKeywordRankBatchMonitor extends GenerateBatchMonitor implements ScheduledTask {

    private static Logger mLogger = LoggerFactory.getLogger(R.BusinessLog.KRS);
    @Autowired
    private BatchRankService mBatchRankService;
    @Autowired
    private CustomerKeywordRankService mCustomerKeywordRankService;

    @Override
    public void execute() {

        /*查询需要生成新的批次的客户关系关键词排名数据*/
        List<CustomerKeywordRank> customerKeywordRanks = mCustomerKeywordRankService.findNeedGenerateBatch();
        mLogger.info("需要生成新批次号的总量：{}",customerKeywordRanks.size());

        generate(customerKeywordRanks, false);
    }

    /**
     * 每个客户取一批满足条件的keywordRank生成批次单号
     */
    public void generate(List<CustomerKeywordRank> customerKeywordRanks, boolean immediate) {
        Date currentTime = new Date();

        /*按客户码分组*/
        Map<String, List<CustomerKeywordRank>> customerListMap = initCustomerListMap(customerKeywordRanks);

        for (String customerCode : customerListMap.keySet()) {
            List<CustomerKeywordRank> rckList = customerListMap.get(customerCode);

            /*生成总单并添加到数据库中*/
            Batch batch = mBatchService.generate(customerCode, R.BatchType.KEYWORD_RANK);
            batch.immediate = immediate ? 1 : 0;
            mBatchService.add(batch);

            /*将批次单号与review建立关系*/
            List<BatchRank> needAddList = new ArrayList<>();
            BatchRank batchRank;
            for (CustomerKeywordRank customerKeywordRank : rckList) {
                batchRank = new BatchRank();
                batchRank.setBatchNum(batch.number);
                batchRank.setKeyword(customerKeywordRank.keyword);
                batchRank.setSiteCode(customerKeywordRank.siteCode);
                batchRank.setAsin(customerKeywordRank.asin);
                batchRank.setDepartmentCode(customerKeywordRank.departmentCode);
                batchRank.setType(R.CrawlType.KEYWORD_RANK);
                batchRank.setPriority(customerKeywordRank.priority);

                needAddList.add(batchRank);

                if (!immediate) {
                    customerKeywordRank.syncTime = currentTime;
                    mCustomerKeywordRankService.update(customerKeywordRank);
                }
            }

            mLogger.info("客户 {} 生成的批次量为：",customerCode,needAddList.size());
            /*添加创建详单信息*/
            mBatchRankService.addAll(needAddList);

            mLogger.info("生成关键词排名搜索批次：成功");
        }
    }
}