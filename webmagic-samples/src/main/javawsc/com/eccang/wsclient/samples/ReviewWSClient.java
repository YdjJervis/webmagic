package com.eccang.wsclient.samples;

import com.eccang.pojo.ReviewQueryReq;
import com.eccang.pojo.ReviewReq;
import com.eccang.wsclient.review.ReviewWSService;
import com.google.gson.Gson;

/**
 * @author Jervis
 * @version V0.20
 * @Description: Review Web Service调用示例
 * @date 2016/11/21 15:09
 */
public class ReviewWSClient {

    public static void main(String[] args) {
//        setPriority();
//        setFrequency();
        add();
//        query();
    }

    private static void query() {

        ReviewQueryReq queryReq = new ReviewQueryReq();
        queryReq.cutomerCode = "EC_001";
        queryReq.platformCode = "ERP";
        queryReq.token = "123456789";

        ReviewQueryReq.Asin asin = queryReq.new Asin();
        asin.asin = "R2V7LL01LD8CLG";
        queryReq.data = asin;

        String json = new ReviewWSService().getReviewWSPort().getReviews(new Gson().toJson(queryReq));
        System.out.println(json);
    }

    private static void add() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_001";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        ReviewReq.Review review1 = reviewReq.new Review();
        review1.priority = 2;
        review1.reviewId = "R2V7LL01LD8CLG";
        review1.siteCode = "US";
        review1.asin = "B0181YRLT4";
        review1.frequency = 2;
        review1.marked = 1;

        reviewReq.data.add(review1);

        String json = new ReviewWSService().getReviewWSPort().addToMonitor(new Gson().toJson(reviewReq));
        System.out.println(json);
    }

    public static void setFrequency() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_001";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        ReviewReq.Review review1 = reviewReq.new Review();
        review1.reviewId = "R2V7LL01LD8CLG";
        review1.frequency = 3;

        reviewReq.data.add(review1);

        String json = new ReviewWSService().getReviewWSPort().setFrequency(new Gson().toJson(reviewReq));
        System.out.println(json);
    }

    public static void setPriority() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_001";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        ReviewReq.Review review1 = reviewReq.new Review();
        review1.reviewId = "R2V7LL01LD8CLG";
        review1.priority = 3;

        reviewReq.data.add(review1);

        String json = new ReviewWSService().getReviewWSPort().setPriority(new Gson().toJson(reviewReq));
        System.out.println(json);
    }
}