package us.codecraft.webmagic.amazon.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.RequestStat;
import us.codecraft.webmagic.samples.amazon.service.RequestStatService;

import java.util.Date;

public class RequestStatServiceTest extends SpringTestCase {

    @Autowired
    RequestStatService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testAdd() {
        RequestStat stat = new RequestStat();

        stat.conditions = "RandomUA&10Thread";
        stat.conditionsCode = DigestUtils.md5Hex(stat.conditions);

        mService.addOnDuplicate(stat);
    }

    @Test
    public void testFind() {
        RequestStat stat = mService.find("f41b3aa00958fed0b3fb8b49fc7938d5");
        stat.extra = "erge";
        stat.firstValidateTime = new Date();
        System.out.println();
        mService.addOnDuplicate(stat);
    }
}
