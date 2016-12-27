package us.codecraft.webmagic.samples.amazon.dao.batch;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchReview;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: Review详单表 DAO
 * @date 2016/11/10 14:45
 */
@Repository
public interface BatchReviewDao extends BaseDao<BatchReview> {

    BatchReview find(BatchReview batchAsin);

    BatchReview findByReviewID(String batchNum, String reviewID);

    List<BatchReview> findAllByBatchNum(String batchNumber);

    List<BatchReview> findNotCrawled();

    float findAverageProgress(String batchNum);
}