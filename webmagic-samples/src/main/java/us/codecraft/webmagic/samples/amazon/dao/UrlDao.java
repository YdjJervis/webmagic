package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Url Dao
 */
@Repository
public interface UrlDao<T> {

    /**
     * @return 查询所有没爬取过和爬取失败的URL
     */
    List<T> findAll();

    /**
     * @return ASIN code
     */
    void update(T url);

    long addAll(List<T> urlList);
}
