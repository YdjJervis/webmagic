package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.batch.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchRank;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerRankKeyword;
import us.codecraft.webmagic.samples.amazon.service.batch.BatchRankService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerRankKeywordService;
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
public class GenerateKeywordRankBatchMonitor extends GenerateBatchMonitor implements ScheduledTask {

    @Autowired
    private BatchRankService mBatchRankService;
    @Autowired
    private CustomerRankKeywordService mCustomerRankKeywordService;

    @Override
    public void execute() {
        generateKeywordRankBatch();
    }

    /**
     * 每个客户取一批满足条件的keywordRank生成批次单号
     */
    public void generateKeywordRankBatch() {
        Date currentTime = new Date();

        /*查询需要生成新的批次的客户关系关键词排名数据*/
        List<CustomerRankKeyword> customerRankKeywords = mCustomerRankKeywordService.findNeedGenerateBatch();
        mLogger.info("需要生成新批次号的总量：" + customerRankKeywords.size());

        /*按客户码分组*/
        Map<String, List<CustomerRankKeyword>> customerListMap = initCustomerListMap(customerRankKeywords);

        for (String customerCode : customerListMap.keySet()) {
            List<CustomerRankKeyword> rckList = customerListMap.get(customerCode);

            /*生成总单并添加到数据库中*/
            Batch batch = mBatchService.generate(customerCode, 4);
            mBatchService.add(batch);

            /*将批次单号与review建立关系*/
            List<BatchRank> needAddList = new ArrayList<>();
            BatchRank batchRank;
            for (CustomerRankKeyword customerRankKeyword : rckList) {
                batchRank = new BatchRank();
                batchRank.setBatchNum(batch.number);
                batchRank.setKeyword(customerRankKeyword.getKeyword());
                batchRank.setSiteCode(customerRankKeyword.getSiteCode());
                batchRank.setAsin(customerRankKeyword.getAsin());
                batchRank.setDepartmentCode(customerRankKeyword.getDepartmentCode());
                batchRank.setType(5);

                needAddList.add(batchRank);

                customerRankKeyword.setSyncTime(currentTime);
                mCustomerRankKeywordService.update(customerRankKeyword);
            }

            mLogger.info("客户 " + customerCode + " 生成的批次量为：" + needAddList.size());
            /*添加创建详单信息*/
            mBatchRankService.addAll(needAddList);

            mLogger.info("生成关键词排名搜索批次：成功");
        }
    }
}