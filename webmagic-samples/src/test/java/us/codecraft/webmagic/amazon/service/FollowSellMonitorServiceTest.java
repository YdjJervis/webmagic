package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.FollowSellMonitor;
import us.codecraft.webmagic.samples.amazon.service.FollowSellMonitorService;

import java.util.ArrayList;
import java.util.List;

public class FollowSellMonitorServiceTest extends SpringTestCase {

    @Autowired
    FollowSellMonitorService mService;

    @Test
    public void testAddAll() {

        List<FollowSellMonitor> list = new ArrayList<FollowSellMonitor>();
        FollowSellMonitor sell = new FollowSellMonitor();
        sell.siteCode = "US";
        sell.asin = "B019ZCMROK";
        list.add(sell);

        mService.addAll(list);
    }

    @Test
    public void testFindNotParsed(){
        System.out.println(mService.findNotParsed());
    }
}
