package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * Asin Dao
 */
@Repository
public interface AsinDao extends BaseDao<Asin> {

    long updateSyncTime(Asin asin);
}
