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
        review.siteCode = "CN";
        review.reviewId = "R1UIT94RIP82Q3";
        review.content = "床前明月光，地上***";
        review.personId = "personID001";
        review.title = "AAAA";
        return review;
    }

    @Test
    public void testFindLastReview(){
        System.out.println(mReviewService.findLastReview("B0169X2S60"));
    }

    @Test
    public void testFindStarReviewCount(){
        System.out.println(mReviewService.findStarReviewCount("B01M4G902N"));
    }

    @Test
    public void testUpdate(){
        Review review = mReviewService.findByReviewId("R1UIT94RIP82Q3");
        review.extra = "Extra001";
        mReviewService.update(review);
    }
}
