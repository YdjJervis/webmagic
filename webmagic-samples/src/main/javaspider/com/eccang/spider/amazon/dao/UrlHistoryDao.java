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
public interface UrlHistoryDao extends BaseDao<Url> {

    List<Url> findByAsin(String siteCode, String asin);
}
