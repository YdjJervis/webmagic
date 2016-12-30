package us.codecraft.webmagic.amazon.monitor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.monitor.ParseUrlKeywordRankMonitor;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/29 10:14
 */
public class ParseUrlMonitorTest extends SpringTestCase {
    @Autowired
    ParseUrlKeywordRankMonitor mParseUrlKeywordRankMonitor;

    @Test
    public void parseUrlKeywordRankMonitorTest() {
        mParseUrlKeywordRankMonitor.execute();
    }
}