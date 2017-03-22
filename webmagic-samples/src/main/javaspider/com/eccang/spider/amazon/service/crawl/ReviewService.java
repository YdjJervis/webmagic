package com.eccang.spider.amazon.service.crawl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.crawl.ReviewDao;
import com.eccang.spider.amazon.pojo.batch.BatchAsinExtra;
import com.eccang.spider.amazon.pojo.crawl.Review;
import com.eccang.spider.amazon.pojo.StarReviewCount;

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

        if (!isExist(review.reviewId)) {
            mReviewDao.add(review);
        } else {
            mReviewDao.update(review);
        }
    }

    public void addAll(List<Review> reviewList) throws Exception{
        List<Review> newList = new ArrayList<>();
        for (Review review : reviewList) {
            if (!isExist(review.reviewId)) {
                newList.add(review);
            } else {
                update(review);
            }
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            mReviewDao.addAll(newList);
        }
    }

    public void update(Review review) {
        mReviewDao.update(review);
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
     *
     * @param review 附带筛选条件的对象
     */
    public List<Review> findAll(Review review) {
        return mReviewDao.findAll(review);
    }

    /**
     * 返回给定条件的评论数据
     */
    public int findAllCount(Review review) {
        return mReviewDao.findAllCount(review);
    }

    public List<Review> findByIdAndRootAsin(String reviewId, String rootAsin) {
        return mReviewDao.findByIdAndRootAsin(reviewId, rootAsin);
    }

    /**
     * 根据指定根ASIN和星级的所有评论
     * @param rootAsin 根ASIN
     * @param star 星级
     */
    public List<BatchAsinExtra> findAll(String rootAsin, int star) {
        return mReviewDao.findAllByStar(rootAsin,star);
    }

}
