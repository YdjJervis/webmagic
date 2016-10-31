package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.ReviewDao;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.StarReviewCount;

import java.util.ArrayList;
import java.util.List;

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

    public void addAll(List<Review> reviewList) {
        List<Review> newList = new ArrayList<Review>();
        for (Review review : reviewList) {
            if (!isExist(review.sarReviewId)) {
                newList.add(review);
            } else {
                mReviewDao.update(review);
            }
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            mReviewDao.addAll(newList);
        }
    }

    public Review findByReviewId(String reviewId) {
        return mReviewDao.findByReviewId(reviewId);
    }

    public List<Review> findLastReview(String asin) {
        return mReviewDao.findLastReview(asin);
    }

    public List<StarReviewCount> findStarReviewCount(String asin) {
        return mReviewDao.findStarReviewCount(asin);
    }

    public boolean isExist(String reviewID) {
        return findByReviewId(reviewID) != null;
    }

}
