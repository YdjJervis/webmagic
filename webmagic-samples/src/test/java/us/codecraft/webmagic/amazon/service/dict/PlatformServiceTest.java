package us.codecraft.webmagic.amazon.service.dict;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.service.dict.PlatformService;

public class PlatformServiceTest extends SpringTestCase {

    @Autowired
    PlatformService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testFindByCode() {
        mLogger.info(mService.findByCode("ERP"));
    }

}
