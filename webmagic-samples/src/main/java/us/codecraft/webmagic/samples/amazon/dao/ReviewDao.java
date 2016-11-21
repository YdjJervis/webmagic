package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.StarReviewCount;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

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

    List<Review> findLastReview(String asin);

    List<StarReviewCount> findStarReviewCount(String asin);

    List<Review> findAll(Review review);
}
