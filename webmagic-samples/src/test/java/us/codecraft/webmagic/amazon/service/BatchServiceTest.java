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
            asin.asin = importAsin.asin;
            list.add(asin);
        }

        mService.addBatch("AA", list);
    }

    @Test
    public void testReviewMonitorBatch() {
        List<Review> list = new ArrayList<Review>();

        Review review = new Review();
        review.priority = 1;
        review.reviewId = "R2V7LL01LD8CRA";
        review.siteCode = "UK";

        list.add(review);

        mService.addMonitor("BB", list);
    }

    @Test
    public void testAdd() {
        Batch batch = new Batch();
        batch.number = "fewef";
        mService.add(batch);
    }

    @Test
    public void testFindByStatus(){
        System.out.println(mService.findByStatus(0));
    }
}
