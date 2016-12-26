package us.codecraft.webmagic.samples.amazon.dao.batch;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchRank;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/26 16:54
 */
@Repository
public interface BatchRankDao {

    void addAll(List<BatchRank> batchRanks);

    void update(BatchRank batchRank);

    List<BatchRank> findByBatch(String batchNum);

}