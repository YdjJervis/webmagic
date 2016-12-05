package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.BatchAsin;
import us.codecraft.webmagic.samples.amazon.service.BatchAsinService;

import java.util.ArrayList;
import java.util.List;

public class BatchAsinServiceTest extends SpringTestCase {

    @Autowired
    BatchAsinService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testAddAll() {

        List<BatchAsin> batchAsinList = new ArrayList<BatchAsin>();

        BatchAsin ba = new BatchAsin();
        ba.siteCode = "CN";
        ba.batchNumber = "EC353945830";
        ba.asin = "B0FUHIKUEHF";
        ba.rootAsin = "B0JFJGEOFJF";

        batchAsinList.add(ba);

        mService.addAll(batchAsinList);
    }

    @Test
    public void testExist() {
        BatchAsin ba = new BatchAsin();
        ba.batchNumber = "EC353945830";
        ba.asin = "B0FUHIKUEHF";


        System.out.println(mService.isExist(ba));
    }

    @Test
    public void testFindByBatchNum() {
        System.out.println(mService.findAllByBatchNum("BN001"));
    }

    @Test
    public void testFindByAsin() {
        System.out.println(mService.findAllByAsin("", "US", "ASIN001"));
    }

    @Test
    public void testUpdate() {
        List<BatchAsin> list = mService.findAllByAsin("", "US", "ASIN001");
        for (BatchAsin batchAsin : list) {
            batchAsin.crawled = 1;
            mService.update(batchAsin);
        }
    }

}
