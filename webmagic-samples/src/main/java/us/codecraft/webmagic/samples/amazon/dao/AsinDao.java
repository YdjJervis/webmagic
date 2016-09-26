package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;

import java.util.List;

/**
 * Asin Dao
 */
@Repository
public interface AsinDao {

    /**
     * @return 查询所有没爬取过的
     */
    List<Asin> findAll();

    /**
     * @return ASIN code
     */
    void update(Asin asin);
}
