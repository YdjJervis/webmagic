package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Asin Dao层
 * @date 2016/10/11 18:00
 */
@Repository
public interface AsinDao extends BaseDao<Asin> {

    long updateSyncTime(Asin asin);

    Asin findByAsin(String asin);

    /**
     * @return 爬取进度为1的Asin列表
     */
    List<Asin> findCrawledAll();
}
