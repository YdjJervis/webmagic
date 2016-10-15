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
public interface UrlDao extends BaseDao<Url> {

    /**
     * @param type 抓取类型。0-抓Review
     * @return 状态码不为200的所有Url
     */
    List<Url> findByType(int type);

    /**
     * @param asin ASIN码
     * @return 指定ASIN的Url列表
     */
    List<Url> findByAsin(String asin);

    List<Url> findMonitorUrlList();

    /**
     * @param asin ASIN码
     * @return 指定ASIN的Url列表
     */
    List<Url> findUpdateCrawl(String asin);
}
