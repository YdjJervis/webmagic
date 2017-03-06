package com.eccang.spider.amazon.dao;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.base.dao.BaseDao;

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

    List<Url> findTop100();

    /**
     * @return 指定站点的ASIN的Url列表
     */
    List<Url> find(String batchNum, String siteCode, String asin, int type);

    void deleteByAsin(String siteCode, String asin);

    /**
     * 重置URL爬取状态
     */
    void resetStatus();

    void deleteByUrlMd5(String urlMd5);

    Url findByUrlMd5(String urlMd5);

    int findByUrlMD5Count(String urlMD5);

    /**
     * @param type 0-全量；1-监听；2-更新
     */
    void deleteByType(int type);

    List<Url> findByBatchNum(String batchNumber);

    List<Url> findByBatchNumAndSite(String batchNum, String siteCode);

    void deleteByBNSC(String batchNum, String siteCode);
}
