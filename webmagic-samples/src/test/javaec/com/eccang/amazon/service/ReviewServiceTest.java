package com.eccang.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.crawl.Review;
import com.eccang.spider.amazon.service.crawl.ReviewService;

import java.util.ArrayList;
import java.util.List;

public class ReviewServiceTest extends SpringTestCase {

    @Autowired
    ReviewService mReviewService;

    @Test
    public void addTest() {

        Review review = getReview();

        mReviewService.add(review);
    }

    @Test
    public void addAllTest() {

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
    public void testFindLastReview() {
        System.out.println(mReviewService.findLastReview("B0169X2S60"));
    }

    @Test
    public void testFindStarReviewCount() {
        System.out.println(mReviewService.findStarReviewCount("B01M4G902N"));
    }

    @Test
    public void testUpdate() {
        Review review = mReviewService.findByReviewId("R1UIT94RIP82Q3");
        review.extra = "Extra001";
        mReviewService.update(review);
    }

    @Test
    public void testFindAll() {
        System.out.println(mReviewService.findAll("B01G38UKDO", 3));
    }

    @Test
    public void findAll() {
        Review review = new Review();
        review.rootAsin = "B0181YRLT4";
        review.siteCode = "CN";
        review.pageNum = 0;
        review.pageSize = 5;
        review.starList.add(1);
        review.starList.add(2);
        List<Review> reviews = mReviewService.findAll(review);

        for (Review review1 : reviews) {
            System.out.println(review1);
        }

    }

    @Test
    public void findAllCount() {
        Review review = new Review();
        review.rootAsin = "B0181YRLT4";
        review.siteCode = "CN";
        review.starList.add(2);
        review.starList.add(1);
        System.out.println(mReviewService.findAllCount(review));
    }

    @Test
    public void findByIdAndRootAsinTest() {
        List<Review> reviews = mReviewService.findByIdAndRootAsin("asdfsad", "B0181YRLT4");
        for (Review review : reviews) {
            System.out.println(review);
        }
    }
}
