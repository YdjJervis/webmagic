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
     * @return 指定站点的ASIN的Url列表
     */
    List<Url> findByAsin(String batchNum, String siteCode, String asin);

    List<Url> findMonitorUrlList();

    /**
     * @return 指定ASIN的Url列表
     */
    List<Url> findUpdateCrawl(String siteCode, String asin);

    void deleteByAsin(String siteCode, String asin);

    /**
     * 重置URL爬取状态
     */
    void resetStatus();

    void deleteByUrlMd5(String urlMd5);

    Url findByUrlMd5(String urlMd5);

    /**
     * @param type 0-全量；1-监听；2-更新
     */
    void deleteByType(int type);

    /**
     * 更改指定ASIN对应URL的优先级
     */
    void updatePriority(String asin, int priority);

    void updateMonitorPriority(String reviewID, int priority);

    void deleteOne(String batchNum, String siteCode, String asin);

    List<Url> findByBatchNum(String batchNumber);
}
