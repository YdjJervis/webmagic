package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * Url Dao
 */
@Repository
public interface UrlDao extends BaseDao<Url> {

    /**
     * @param type 抓取类型。0-抓Review
     * @return 状态码不为200的所有Url
     */
    List<Url> find(int type);
}
