package us.codecraft.webmagic.amazon.monitor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.monitor.GenerateBatchMonitor;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/7 11:33
 */
public class GenerateBatchMonitorTest extends SpringTestCase {
    @Autowired
    GenerateBatchMonitor mGenerateBatchMonitor;

    @Test
    public void generateReviewMonitorBathTest() {
        mGenerateBatchMonitor.generateReviewMonitorBatch();
    }

}