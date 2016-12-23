package com.eccang.wsclient.samples;

import com.eccang.pojo.CustomerReviewUpdateReq;
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
//        add();
        query();
//        setCrawl();
//        findReviewByIdAndAsin();
    }

    private static void query() {

        ReviewQueryReq queryReq = new ReviewQueryReq();
        queryReq.cutomerCode = "EC_001";
        queryReq.platformCode = "ERP";
        queryReq.token = "123456789";

        ReviewQueryReq.Asin asin = queryReq.new Asin();
        asin.asin = "B0181YRLT4";
        asin.siteCode = "CN";
        asin.level = "0-1-1-1-1";
        asin.pageSize = 2;
        asin.personID = "00215498";
        asin.experience = "no";
        asin.pageNum = 1;
        queryReq.data = asin;

        String queryReqStr = new Gson().toJson(queryReq);
        String json = new ReviewWSService().getReviewWSPort().getReviewsByAsin(queryReqStr);
        System.out.println(json);
    }

    private static void add() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_001";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        ReviewReq.Review review1 = reviewReq.new Review();
        review1.priority = 4;
        review1.reviewId = "R2V7LL01LD8CLP1";
        review1.siteCode = "DE";
        review1.asin = "B0181YRLT4";
        review1.frequency = 2;

        reviewReq.data.add(review1);
        String reviewReqStr = new Gson().toJson(reviewReq);
        String json = new ReviewWSService().getReviewWSPort().addToMonitor(reviewReqStr);
        System.out.println(json);
    }

    public static void setFrequency() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_001";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        ReviewReq.Review review1 = reviewReq.new Review();
        review1.reviewId = "R2V7LL01LD8CLG";
        review1.frequency = 4;

        reviewReq.data.add(review1);
        String reviewReqStr = new Gson().toJson(reviewReq);
        String json = new ReviewWSService().getReviewWSPort().setFrequency(reviewReqStr);
        System.out.println(json);
    }

    public static void setPriority() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_001";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        ReviewReq.Review review1 = reviewReq.new Review();
        review1.reviewId = "R2V7LL01LD8CLG";
        review1.priority = 5;

        reviewReq.data.add(review1);

        String reviewReqStr = new Gson().toJson(reviewReq);
        String json = new ReviewWSService().getReviewWSPort().setPriority(reviewReqStr);
        System.out.println(json);
    }

    public static void setCrawl() {
        CustomerReviewUpdateReq customerReviewUpdateReq = new CustomerReviewUpdateReq();
        customerReviewUpdateReq.cutomerCode = "EC_001";
        customerReviewUpdateReq.platformCode = "ERP";
        customerReviewUpdateReq.token = "123456789";

        CustomerReviewUpdateReq.CustomerReview customerReview = customerReviewUpdateReq.new CustomerReview();
        customerReview.reviewId = "R2V7LL01LD8CLP1";
        customerReview.status = 0;

        customerReviewUpdateReq.data.add(customerReview);

        String customerReviewUpdateReqStr = new Gson().toJson(customerReviewUpdateReq);

        String json = new ReviewWSService().getReviewWSPort().setReviewMonitor(customerReviewUpdateReqStr);
        System.out.println(json);
    }

    private static void findReviewByIdAndAsin() {
        ReviewQueryReq queryReq = new ReviewQueryReq();
        queryReq.cutomerCode = "EC_001";
        queryReq.platformCode = "ERP";
        queryReq.token = "123456789";

        ReviewQueryReq.Asin asin = queryReq.new Asin();
        asin.asin = "B0181YRLT4";
        asin.reviewId = "asdfsad";
        asin.siteCode = "CN";
        queryReq.data = asin;
        String queryReqStr = new Gson().toJson(queryReq);
        String json = new ReviewWSService().getReviewWSPort().getReviewById(queryReqStr);
        System.out.println(json);
    }
}