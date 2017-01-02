package com.eccang.amazon.monitor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.monitor.GenerateAsinBatchMonitor;
import com.eccang.spider.amazon.monitor.GenerateKeywordRankBatchMonitor;
import com.eccang.spider.amazon.monitor.GenerateReviewBatchMonitor;

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

}