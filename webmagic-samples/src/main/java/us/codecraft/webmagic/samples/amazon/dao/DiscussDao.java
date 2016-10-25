package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;

import java.util.List;

/**
 * 评论DAO
 */
@Repository
public interface DiscussDao {

    List<Discuss> findAll();

    List<Discuss> findAllByAsin(String asin);

    /**
     * @return ASIN code
     */
    int add(Discuss discuss);

    int addAll(List<Discuss> discussList);


}
