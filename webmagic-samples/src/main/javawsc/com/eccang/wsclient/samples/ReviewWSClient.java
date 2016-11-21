package com.eccang.wsclient.samples;

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

        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_001";
        reviewReq.platformCode = "system";
        reviewReq.token = "123456789";

        ReviewReq.Review review1 = reviewReq.new Review();
        review1.priority = 1;
        review1.reviewID = "R2V7LL01LD8CRA";
        review1.siteCode = "UK";

        reviewReq.data.add(review1);

        String json = new ReviewWSService().getReviewWSPort().addToMonitor(new Gson().toJson(reviewReq));
        System.out.println(json);
    }
}