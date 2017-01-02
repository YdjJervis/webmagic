package com.eccang.spider.amazon.dao.crawl;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.batch.BatchAsinExtra;
import com.eccang.spider.amazon.pojo.crawl.Review;
import com.eccang.spider.amazon.pojo.StarReviewCount;
import com.eccang.spider.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论Dao
 * @date 2016/10/11 18:00
 */
@Repository
public interface ReviewDao extends BaseDao<Review> {

    Review findByReviewId(String reviewId);

    List<Review> findLastReview(String rootAsin);

    List<StarReviewCount> findStarReviewCount(String asin);

    List<Review> findAll(Review review);

    List<Review> findByIdAndRootAsin(String reviewId, String rootAsin);

    int findAllCount(Review review);

    List<BatchAsinExtra> findAllByStar(String rootAsin, int star);
}
