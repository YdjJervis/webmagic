package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.batch.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchRank;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerKeywordRank;
import us.codecraft.webmagic.samples.amazon.service.batch.BatchRankService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerKeywordRankService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

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

    @Autowired
    private BatchRankService mBatchRankService;
    @Autowired
    private CustomerKeywordRankService mCustomerKeywordRankService;

    @Override
    public void execute() {
        generateKeywordRankBatch();
    }

    /**
     * 每个客户取一批满足条件的keywordRank生成批次单号
     */
    private void generateKeywordRankBatch() {
        Date currentTime = new Date();

        /*查询需要生成新的批次的客户关系关键词排名数据*/
        List<CustomerKeywordRank> customerKeywordRanks = mCustomerKeywordRankService.findNeedGenerateBatch();
        mLogger.info("需要生成新批次号的总量：" + customerKeywordRanks.size());

        /*按客户码分组*/
        Map<String, List<CustomerKeywordRank>> customerListMap = initCustomerListMap(customerKeywordRanks);

        for (String customerCode : customerListMap.keySet()) {
            List<CustomerKeywordRank> rckList = customerListMap.get(customerCode);

            /*生成总单并添加到数据库中*/
            Batch batch = mBatchService.generate(customerCode, R.BatchType.KEYWORD_RANK);
            mBatchService.add(batch);

            /*将批次单号与review建立关系*/
            List<BatchRank> needAddList = new ArrayList<>();
            BatchRank batchRank;
            for (CustomerKeywordRank customerKeywordRank : rckList) {
                batchRank = new BatchRank();
                batchRank.setBatchNum(batch.number);
                batchRank.setKeyword(customerKeywordRank.getKeyword());
                batchRank.setSiteCode(customerKeywordRank.getSiteCode());
                batchRank.setAsin(customerKeywordRank.getAsin());
                batchRank.setDepartmentCode(customerKeywordRank.getDepartmentCode());
                batchRank.setType(R.CrawlType.KEYWORD_RANK);
                batchRank.setPriority(customerKeywordRank.getPriority());

                needAddList.add(batchRank);

                customerKeywordRank.setSyncTime(currentTime);
                mCustomerKeywordRankService.update(customerKeywordRank);
            }

            mLogger.info("客户 " + customerCode + " 生成的批次量为：" + needAddList.size());
            /*添加创建详单信息*/
            mBatchRankService.addAll(needAddList);

            mLogger.info("生成关键词排名搜索批次：成功");
        }
    }
}