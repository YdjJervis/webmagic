package us.codecraft.webmagic.samples.amazon.dao.batch;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchFollowSell;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 跟卖详单 DAO
 * @date 2016/11/10 14:45
 */
@Repository
public interface BatchFollowSellDao extends BaseDao<BatchFollowSell> {


    BatchFollowSell find(String batchNum, String siteCode, String asin);

    List<BatchFollowSell> findAllByBatchNum(String batchNum);

    List<BatchFollowSell> findNotCrawled();

    float findAverageProgress(String batchNum);
}