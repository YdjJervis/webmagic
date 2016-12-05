package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次DAO
 * @date 2016/11/10 14:45
 */
@Repository
public interface BatchDao extends BaseDao<Batch> {

    Batch findByBatchNumber(String batchNumber);

    List<Batch> findByStatus(int status);
}