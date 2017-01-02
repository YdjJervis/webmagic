package com.eccang.amazon.service.batch;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.dao.ImportAsinDao;
import com.eccang.spider.amazon.pojo.*;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.service.batch.BatchService;

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
