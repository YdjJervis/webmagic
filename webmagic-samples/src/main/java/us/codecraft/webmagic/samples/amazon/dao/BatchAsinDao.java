package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.BatchAsin;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次产生的ASIN进度 DAO
 * @date 2016/11/10 14:45
 */
@Repository
public interface BatchAsinDao extends BaseDao<BatchAsin> {

    BatchAsin find(BatchAsin batchAsin);

    BatchAsin findAllByAsin(String batchNum, String siteCode, String asin);

    List<BatchAsin> findAllByBatchNum(String batchNumber);

    List<BatchAsin> findByStatus(int sta);

    float findAverageProgress(String batchNumber);

    List<BatchAsin> findNotCrawledMainPage();

    List<BatchAsin> findNotCrawledReview();

    List<BatchAsin> findNotUpdatedReview();
}