package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.review.CusReviewUpdateReq;
import com.eccang.pojo.review.ReviewQueryReq;
import com.eccang.pojo.review.ReviewReq;
import com.eccang.spider.amazon.service.crawl.ReviewService;
import com.eccang.wsclient.review.ReviewWSService;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2016/12/22 13:52
 */
public class ReviewWSTest extends SpringTestCase {

    @Autowired
    private ReviewService mReviewService;

    @Test
    public void query() {

        ReviewQueryReq queryReq = new ReviewQueryReq();
        queryReq.customerCode = "EC_001";
        queryReq.platformCode = "ERP";
        queryReq.token = "123456789";

        ReviewQueryReq.Asin asin = queryReq.new Asin();
        asin.asin = "B001B8R03U";
        asin.siteCode = "US";
        queryReq.data = asin;

        String json = new ReviewWSService().getReviewWSPort().getReviewsByAsin(new Gson().toJson(queryReq));
        System.out.println(json);
    }

    @Test
    public void addToMonitor() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.customerCode = "EC_002";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";


        /*Review re = new Review();
        re.star = 1;
        List<Review> reviewList = mReviewService.findAll(re);
        for (Review reviewLoop : reviewList) {
            ReviewReq.Review review = reviewReq.new Review();
            review.reviewId = "Tx164I0R2U1QE3";
            review.siteCode = "US";
            review.asin = "B00HYAL84G";
            reviewReq.data.add(review);
        }*/

//        for (int i = 0; i < 10; i++) {
        ReviewReq.Review review = reviewReq.new Review();
        review.reviewId = "Tx164I0R2U1QE" + 11;
        review.siteCode = "US";
        review.asin = "B00HYAL84G";
        reviewReq.data.add(review);
//        }

        String json = new ReviewWSService().getReviewWSPort().addToMonitor(new Gson().toJson(reviewReq));
        System.out.println(json);
    }

    @Test
    public void addToMonitorMany(){
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.customerCode = "EC_001";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        ReviewReq.Review review = reviewReq.new Review();
        review.reviewId = "R1XILAPZ22PKDI";
        review.siteCode = "UK";
        review.asin = "B01G38UKDO";
        reviewReq.data.add(review);

        review = reviewReq.new Review();
        review.reviewId = "R3N2ODIAYVFTAS";
        review.siteCode = "US";
        review.asin = "B01LY3AMY8";
        reviewReq.data.add(review);

        String json = new ReviewWSService().getReviewWSPort().addToMonitor(new Gson().toJson(reviewReq));
        System.out.println(json);
    }

    @Test
    public void setReviewMonitor() {
        CusReviewUpdateReq req = new CusReviewUpdateReq();

        req.customerCode = "EC_002";
        req.platformCode = "ERP";
        req.token = "123456789";

        CusReviewUpdateReq.CustomerReview review1 = req.new CustomerReview();
        review1.reviewId = "Tx164I0R2U1QE4";
        review1.crawl = 1;
        req.data.add(review1);

        req.new CustomerReview();
        review1.reviewId = "Tx164I0R2U1QE4";
        review1.crawl = 1;
        req.data.add(review1);

        String json = new ReviewWSService().getReviewWSPort().setReviewMonitor(new Gson().toJson(req));
        System.out.println(json);
    }

    @Test
    public void getReviewStatusTest() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.customerCode = "EC_001";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        String json = new ReviewWSService().getReviewWSPort().getReviewsStatus(new Gson().toJson(reviewReq));
        System.out.println(json);
    }
}
