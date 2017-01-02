package com.eccang.amazon.monitor;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.monitor.ParseUrlKeywordRankMonitor;

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