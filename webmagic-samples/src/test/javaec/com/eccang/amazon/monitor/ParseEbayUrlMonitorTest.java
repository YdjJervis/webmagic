package com.eccang.amazon.monitor;

import com.eccang.spider.amazon.monitor.ParseUrlFollowSellMonitor;
import com.eccang.spider.amazon.processor.Top100Processor;
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
public class ParseEbayUrlMonitorTest extends SpringTestCase {
    @Autowired
    ParseUrlKeywordRankMonitor mParseUrlKeywordRankMonitor;
    @Autowired
    ParseUrlFollowSellMonitor mParseUrlFollowSellMonitor;
    @Autowired
    Top100Processor mTop100Processor;

    @Test
    public void parseUrlKeywordRankMonitorTest() {
        mParseUrlKeywordRankMonitor.execute();
    }

    @Test
    public void parseUrlFollowSellMonitorTest() {
        mParseUrlFollowSellMonitor.execute();
    }
}