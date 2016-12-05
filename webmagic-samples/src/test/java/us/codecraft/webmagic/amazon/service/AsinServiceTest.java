package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.service.AsinService;

public class AsinServiceTest extends SpringTestCase {

    @Autowired
    AsinService mAsinService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testFindAll() {
        mLogger.info(mAsinService.findAll());
    }

    @Test
    public void testGetFilter() {
        System.out.println(mAsinService.getUpdateFilters("0-0-1-1-1"));
    }

    @Test
    public void testUpdate() {
        Asin asin = mAsinService.findByAsin("UK", "B01LXA42FB");
        asin.onSale = 0;
        mAsinService.update(asin);
    }
}
