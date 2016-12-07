package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.RankSearchKeyword;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 *          <p>
 *          2016/12/2 15:26
 */
@Repository
public interface RankSearchKeywordDao {

    void add(RankSearchKeyword rankSearchKeyword);

    void deleteById(int id);

    void updateById(RankSearchKeyword rankSearchKeyword);

    RankSearchKeyword findById(int id);

    List<RankSearchKeyword> findAll();
}
