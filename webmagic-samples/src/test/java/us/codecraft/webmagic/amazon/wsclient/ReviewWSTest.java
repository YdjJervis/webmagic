package us.codecraft.webmagic.amazon.wsclient;

import com.eccang.pojo.ReviewQueryReq;
import com.eccang.pojo.ReviewReq;
import com.eccang.wsclient.review.ReviewWSService;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.service.ReviewService;

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
        queryReq.cutomerCode = "EC_002";
        queryReq.platformCode = "ERP";
        queryReq.token = "123456789";

        ReviewQueryReq.Asin asin = queryReq.new Asin();
        asin.asin = "B00HYAL84G";
        asin.siteCode = "US";
        queryReq.data = asin;

        String json = new ReviewWSService().getReviewWSPort().getReviewsByAsin(new Gson().toJson(queryReq));
        System.out.println(json);
    }

    @Test
    public void add() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_002";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";


        Review re = new Review();
        re.star = 1;
//        List<Review> reviewList = mReviewService.findAll(re);
//        for (Review reviewLoop : reviewList) {
            ReviewReq.Review review = reviewReq.new Review();
            review.priority = 2;
            review.reviewId = "Tx164I0R2U1QE3";
            review.siteCode = "US";
            review.asin = "B00HYAL84G";
            review.frequency = 4;
            reviewReq.data.add(review);
//        }

        String json = new ReviewWSService().getReviewWSPort().addToMonitor(new Gson().toJson(reviewReq));
        System.out.println(json);
    }

    @Test
    public void setFrequency() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_002";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        ReviewReq.Review review1 = reviewReq.new Review();
        review1.reviewId = "Tx164I0R2U1QE3";
        review1.frequency = 8;

        reviewReq.data.add(review1);

        String json = new ReviewWSService().getReviewWSPort().setFrequency(new Gson().toJson(reviewReq));
        System.out.println(json);
    }

    @Test
    public void setPriority() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_002";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        ReviewReq.Review review1 = reviewReq.new Review();
        review1.reviewId = "Tx164I0R2U1QE3";
        review1.priority = 3;

        reviewReq.data.add(review1);

        String json = new ReviewWSService().getReviewWSPort().setPriority(new Gson().toJson(reviewReq));
        System.out.println(json);
    }

    @Test
    public void getReviewStatusTest() {
        ReviewReq reviewReq = new ReviewReq();

        reviewReq.cutomerCode = "EC_001";
        reviewReq.platformCode = "ERP";
        reviewReq.token = "123456789";

        String json = new ReviewWSService().getReviewWSPort().getReviewsStatus(new Gson().toJson(reviewReq));
        System.out.println(json);
    }
}
