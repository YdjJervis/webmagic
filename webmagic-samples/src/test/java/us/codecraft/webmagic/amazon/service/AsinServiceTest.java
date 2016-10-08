package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.service.AsinService;

public class AsinServiceTest extends SpringTestCase {

    @Autowired
    AsinService mAsinService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void findTest() {
        mLogger.info(mAsinService.find(3));
    }

    @Test
    public void testUpdateStatus() {
        mAsinService.update(mAsinService.find(3).get(0), 2, 1);
    }
}
