package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.service.UrlService;

public class UrlServiceTest extends SpringTestCase {

    @Autowired
    UrlService mUrlService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void findTest(){
        mLogger.info(mUrlService.findFailures());
    }
}
