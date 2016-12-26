package us.codecraft.webmagic.amazon.service.dict;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.service.dict.SiteService;

public class SiteServiceTest extends SpringTestCase {

    @Autowired
    SiteService mSiteService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void findTest(){
        mLogger.info(mSiteService.find("CN"));
    }
}
