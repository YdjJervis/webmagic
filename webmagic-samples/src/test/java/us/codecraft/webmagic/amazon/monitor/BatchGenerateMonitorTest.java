package us.codecraft.webmagic.amazon.monitor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.monitor.BatchGenerateMonitor;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/7 11:33
 */
public class BatchGenerateMonitorTest extends SpringTestCase {
    @Autowired
    BatchGenerateMonitor mBatchGenerateMonitor;

    @Test
    public void generateReviewMonitorBathTest() {
        mBatchGenerateMonitor.generateReviewMonitorBatch();
    }

}