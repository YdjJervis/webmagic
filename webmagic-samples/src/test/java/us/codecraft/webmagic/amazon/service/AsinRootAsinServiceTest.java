package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.AsinRootAsin;
import us.codecraft.webmagic.samples.amazon.service.AsinRootAsinService;

public class AsinRootAsinServiceTest extends SpringTestCase {

    @Autowired
    AsinRootAsinService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testAdd() {

        AsinRootAsin asinRootAsin = new AsinRootAsin();
        asinRootAsin.asin = "Asin001";
        asinRootAsin.rootAsin = "RootAsin001";
        mService.add(asinRootAsin);
    }

}
