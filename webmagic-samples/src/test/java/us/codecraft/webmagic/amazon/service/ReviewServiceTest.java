package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.service.ReviewService;

import java.util.ArrayList;
import java.util.List;

public class ReviewServiceTest extends SpringTestCase {

    @Autowired
    ReviewService mReviewService;

    @Test
    public void addTest(){

        Review review = getReview();

        mReviewService.add(review);
    }

    @Test
    public void addAllTest(){

        Review review = getReview();
        List<Review> list = new ArrayList<Review>();
        list.add(review);
        mReviewService.addAll(list);
    }

    private Review getReview() {
        Review review = new Review();
        review.basCode = "CN";
        review.sarReviewId = "R1UIT94RIP82Q3";
        review.sarContent = "床前明月光，地上***";
        review.sarPersonId = "personID001";
        review.saaAsin = "Asin_00001";
        review.sarTitle = "AAAA";
        return review;
    }

    @Test
    public void testFindLastReview(){
        System.out.println(mReviewService.findLastReview("B0181YRLT4"));
    }

    @Test
    public void testFindStarReviewCount(){
        System.out.println(mReviewService.findStarReviewCount("B01M4G902N"));
    }
}
