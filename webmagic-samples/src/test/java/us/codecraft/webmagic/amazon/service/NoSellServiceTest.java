package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.service.NoSellService;

public class NoSellServiceTest extends SpringTestCase {

    @Autowired
    NoSellService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testAdd() {
        Asin asin = new Asin();
        asin.siteCode = "US";
        asin.rootAsin = "Asin001";
        mService.add(asin);
    }

}
