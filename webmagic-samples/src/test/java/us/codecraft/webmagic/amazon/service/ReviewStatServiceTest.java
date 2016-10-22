package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewStat;
import us.codecraft.webmagic.samples.amazon.service.ReviewStatService;

public class ReviewStatServiceTest extends SpringTestCase {

    @Autowired
    private ReviewStatService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testAdd() {
        ReviewStat stat = new ReviewStat();
        stat.extra = "star";
        stat.saaAsin = "asin";
        stat.sarsTotalReview = 100;
        stat.sarsAverageStar = 4.5f;
        mService.add(stat);
    }

    @Test
    public void testFind() {
        mLogger.info(mService.find("asin"));
    }
}
