package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Url;

import java.util.List;

/**
 * Url Dao
 */
@Repository
public interface UrlDao {

    /**
     * @return 查询所有没爬取过和爬取失败的URL
     */
    List<Url> findAll();

    /**
     * @return ASIN code
     */
    void update(Url url);

    long addAll(List<Url> urlList);
}
