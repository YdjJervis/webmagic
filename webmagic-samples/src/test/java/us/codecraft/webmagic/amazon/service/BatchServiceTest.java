package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.AsinSource;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.Site;
import us.codecraft.webmagic.samples.amazon.service.BatchService;

import java.util.ArrayList;
import java.util.List;

public class BatchServiceTest extends SpringTestCase {

    @Autowired
    BatchService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testFindByCode() {
        List<Asin> list = new ArrayList<Asin>();

        Asin asin = new Asin();
        asin.saaAsin = "B01KT0ARWG";
        asin.saaPriority = 3;
        asin.saaStar = "1-1-1-1-1";
        asin.site = new Site();
        asin.asinSource = new AsinSource();
        asin.asinSource.baasCode = "SYSTEM";
        asin.site.basCode = "US";

        list.add(asin);

        mService.add("AA",list);
    }

    @Test
    public void testAdd(){
        Batch batch = new Batch();
        batch.number = "fewef";
        mService.add(batch);
    }
}
