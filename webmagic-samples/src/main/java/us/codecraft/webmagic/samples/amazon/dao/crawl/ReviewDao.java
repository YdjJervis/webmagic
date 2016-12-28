package us.codecraft.webmagic.samples.amazon.dao.crawl;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchAsinExtra;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Review;
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

    List<Review> findLastReview(String rootAsin);

    List<StarReviewCount> findStarReviewCount(String asin);

    List<Review> findAll(Review review);

    List<Review> findByIdAndRootAsin(String reviewId, String rootAsin);

    int findAllCount(Review review);

    List<BatchAsinExtra> findAllByStar(String rootAsin, int star);
}
