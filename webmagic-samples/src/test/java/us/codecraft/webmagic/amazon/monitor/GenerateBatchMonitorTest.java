package us.codecraft.webmagic.amazon.monitor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.monitor.GenerateAsinBatchMonitor;
import us.codecraft.webmagic.samples.amazon.monitor.GenerateReviewBatchMonitor;

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

    @Test
    public void generateReviewMonitorBathTest() {
        mGenerateBatchMonitor.execute();
    }

    @Test
    public void generateReviewUpdateBatch() {
        mGenerateReviewBatchMonitor.execute();
    }

}