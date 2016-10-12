package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.ReviewDao;
import us.codecraft.webmagic.samples.amazon.pojo.Review;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Review业务
 * @date 2016/10/11
 */
@Service
public class ReviewService {

    @Autowired
    ReviewDao mReviewDao;

    public void add(Review review) {
        mReviewDao.add(review);
    }

    public Review findByReviewId(String reviewId){
        return mReviewDao.findByReviewId(reviewId);
    }

}
