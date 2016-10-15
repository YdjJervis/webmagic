package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.service.ReviewService;

public class ReviewServiceTest extends SpringTestCase {

    @Autowired
    ReviewService mReviewService;

    @Test
    public void addTest(){

        Review review = new Review();
        review.basCode = "CN";
        review.sarReviewId = "R1UIT94RIP82Q3";
        review.sarContent = "床前明月光，地上***";
        review.sarPersonId = "personID001";
        review.saaAsin = "Asin_00001";

        mReviewService.add(review);
    }

    @Test
    public void testFindLastReview(){
        System.out.println(mReviewService.findLastReview("B0181YRLT4"));
    }
}
