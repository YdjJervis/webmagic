package us.codecraft.webmagic.samples.amazon.dao.crawl;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.KeywordRank;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/27 11:15
 */
@Repository
public interface KeywordRankDao {

    void add(KeywordRank keywordRank);

    void addAll(List<KeywordRank> keywordRanks);

    void updateByObj(KeywordRank keywordRank);

    void updateRankNum(KeywordRank keywordRank);

    List<KeywordRank> findByAsin(String asin);

    KeywordRank findByObj(KeywordRank keywordRank);
}
