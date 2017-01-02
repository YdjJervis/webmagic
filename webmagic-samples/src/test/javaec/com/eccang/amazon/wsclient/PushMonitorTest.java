package com.eccang.amazon.wsclient;

import com.eccang.wsclient.push.PushMonitor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/14 11:51
 */
public class PushMonitorTest extends SpringTestCase {
    @Autowired
    PushMonitor mPushMonitor;

    @Test
    public void Test() {
        mPushMonitor.execute();
    }
}