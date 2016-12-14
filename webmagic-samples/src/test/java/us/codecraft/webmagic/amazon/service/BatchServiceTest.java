package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.ImportAsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.*;
import us.codecraft.webmagic.samples.amazon.service.BatchService;

import java.util.ArrayList;
import java.util.List;

public class BatchServiceTest extends SpringTestCase {

    @Autowired
    BatchService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private ImportAsinDao mImportAsinDao;

    @Test
    public void testGenerateBatchOrder() {
        List<BatchAsin> list = new ArrayList<BatchAsin>();

        List<ImportAsin> importAsinList = mImportAsinDao.findAll(5);
        for (ImportAsin importAsin : importAsinList) {
            BatchAsin batchAsin = new BatchAsin();
            batchAsin.siteCode = importAsin.siteCode;
            batchAsin.asin = importAsin.asin;
            list.add(batchAsin);
        }

        mService.addBatch("AA", list, 0, 0);
    }

    @Test
    public void testAdd() {
        Batch batch = new Batch();
        batch.number = "fewef";
        mService.add(batch);
    }

    @Test
    public void testFindByStatus() {
        System.out.println(mService.findByStatus(0));
    }

    @Test
    public void testUpdate() {
        Batch batch = mService.findByBatchNumber("EC20161214111211328");
        batch.progress = 1;
        mService.update(batch);
    }
}
