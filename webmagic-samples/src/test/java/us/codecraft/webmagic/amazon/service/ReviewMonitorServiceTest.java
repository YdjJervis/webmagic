package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.service.ReviewMonitorService;

public class ReviewMonitorServiceTest extends SpringTestCase {

    @Autowired
    ReviewMonitorService mReviewMonitorService;

    @Test
    public void addTest(){
        mReviewMonitorService.add("123456");
    }

    @Test
    public void testUpdate(){
        mReviewMonitorService.update("123",false);
    }
}
