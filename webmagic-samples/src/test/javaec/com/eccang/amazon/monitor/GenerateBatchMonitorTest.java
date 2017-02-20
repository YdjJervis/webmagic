package com.eccang.amazon.monitor;

import com.eccang.spider.amazon.monitor.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/7 11:33
 */
public class GenerateBatchMonitorTest extends SpringTestCase {

    @Autowired
    private GenerateAsinBatchMonitor mGenerateBatchMonitor;

    @Autowired
    private GenerateReviewBatchMonitor mGenerateReviewBatchMonitor;

    @Autowired
    private GenerateKeywordRankBatchMonitor mGenerateKeywordRankBatchMonitor;

    @Autowired
    private GenerateFollowSellBatchMonitor mGenerateFollowSellBatchMonitor;

    @Autowired
    private GenerateTop100BatchMonitor mGenerateTop100BatchMonitor;

    @Test
    public void generateReviewMonitorBathTest() {
        mGenerateBatchMonitor.execute();
    }

    @Test
    public void generateReviewUpdateBatch() {
        mGenerateReviewBatchMonitor.execute();
    }

    @Test
    public void generateKeywordRankBatchMonitor() {
        mGenerateKeywordRankBatchMonitor.execute();
    }

    @Test
    public void generateFollowSellBatchMonitor() {
        mGenerateFollowSellBatchMonitor.execute();
    }

    @Test
    public void generateTop100Batch() {
        mGenerateTop100BatchMonitor.execute();
    }

}