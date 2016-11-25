package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
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

    public void add(Review review) {

        if (!isExist(review.sarReviewId)) {
            mReviewDao.add(review);
        } else {
            mReviewDao.update(review);
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

    public List<Review> findLastReview(String rootAsin) {
        return mReviewDao.findLastReview(rootAsin);
    }

    public List<StarReviewCount> findStarReviewCount(String rootAsin) {
        return mReviewDao.findStarReviewCount(rootAsin);
    }

    public boolean isExist(String reviewID) {
        return findByReviewId(reviewID) != null;
    }

    /**
     * 返回给定条件的多有评论
     * @param review 附带筛选条件的对象
     */
    public List<Review> findAll(Review review) {
        return mReviewDao.findAll(review);
    }

}
