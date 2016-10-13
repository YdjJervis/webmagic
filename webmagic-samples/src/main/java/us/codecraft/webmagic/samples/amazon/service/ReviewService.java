package us.codecraft.webmagic.samples.amazon.service;

import org.apache.log4j.Logger;
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
    private Logger mLogger = Logger.getLogger(getClass());

    public void add(Review review) {
        try {
            mReviewDao.add(review);
        } catch (Exception e) {
            mLogger.error("评论入库失败，请提供日志给爬虫组...");
            mLogger.error(e);
        }
    }

    public Review findByReviewId(String reviewId){
        return mReviewDao.findByReviewId(reviewId);
    }

}
