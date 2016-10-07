package us.codecraft.webmagic.amazon;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.ReviewDao;
import us.codecraft.webmagic.samples.amazon.pojo.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能概要：DiscussService单元测试
 *
 * @author Jervis
 */
public class ReviewDaoTest extends SpringTestCase {

    @Autowired
    private ReviewDao mDao;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void addTest() {
        Review review = getReview();
        mLogger.info(mDao.add(review));
    }

    private Review getReview() {
        Review review = new Review();
        review.saaAsin = "111111";
        review.sarPersonId = "222222";
        review.sarReviewId = "333333";
        review.basCode = "CN";
        review.sarContent = "天王盖地虎";
        review.sarVersion = "白虎";
        return review;
    }


    @Test
    public void addAllTest(){

        List<Review> list = new ArrayList<Review>();
        list.add(getReview());

        mLogger.info(mDao.addAll(list));
    }

}