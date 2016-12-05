package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Url爬取队列Dao
 * @date 2016/10/11 18:00
 */
@Repository
public interface UrlHistoryDao extends BaseDao<Url> {

    List<Url> findByAsin(String siteCode, String asin);
}
