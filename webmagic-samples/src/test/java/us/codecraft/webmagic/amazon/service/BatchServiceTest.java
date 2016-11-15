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
        List<Asin> list = new ArrayList<Asin>();

        List<ImportAsin> importAsinList = mImportAsinDao.findAll(5);
        for (ImportAsin importAsin : importAsinList) {
            Asin asin = new Asin();
            asin.saaAsin = importAsin.asin;
            asin.saaPriority = 0;
            asin.saaStar = "0-0-1-1-1";
            asin.site = new Site();
            asin.asinSource = new AsinSource();
            asin.asinSource.baasCode = "SYSTEM";
            asin.site.basCode = importAsin.siteCode;
            list.add(asin);
        }

        mService.addBatch("AA", list);
    }

    @Test
    public void testReviewMonitorBatch() {
        List<Review> list = new ArrayList<Review>();

        Review review = new Review();
        review.priority = 1;
        review.sarReviewId = "R2V7LL01LD8CRA";
        review.basCode = "UK";

        list.add(review);

        mService.addMonitor("BB", list);
    }

    @Test
    public void testAdd() {
        Batch batch = new Batch();
        batch.number = "fewef";
        mService.add(batch);
    }
}
