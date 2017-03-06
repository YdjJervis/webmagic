package com.eccang.amazon.monitor;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.monitor.ParseUrlReviewBatchMonitor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/3/2 11:11
 */
public class ParseUrlReviewBatchMonitorTest extends SpringTestCase {

    @Autowired
    private ParseUrlReviewBatchMonitor mMonitor;

    @Test
    public void execute(){
        mMonitor.execute();
    }
}
