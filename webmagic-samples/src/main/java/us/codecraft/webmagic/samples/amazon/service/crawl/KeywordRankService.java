package us.codecraft.webmagic.samples.amazon.service.crawl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.crawl.KeywordRankDao;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.KeywordRank;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/27 11:37
 */
@Service
public class KeywordRankService {

    @Autowired
    KeywordRankDao mKeywordRankDao;

    public void add(KeywordRank keywordRank) {
        mKeywordRankDao.add(keywordRank);
    }

    public void addAll(List<KeywordRank> keywordRanks) {
        mKeywordRankDao.addAll(keywordRanks);
    }

    public void updateByObj(KeywordRank keywordRank) {
        mKeywordRankDao.updateByObj(keywordRank);
    }

    public void updateRankNum(KeywordRank keywordRank) {
        mKeywordRankDao.updateRankNum(keywordRank);
    }

    public List<KeywordRank> findByAsin(String asin) {
        return mKeywordRankDao.findByAsin(asin);
    }

    public KeywordRank findByObj(KeywordRank keywordRank) {
        return mKeywordRankDao.findByObj(keywordRank);
    }

}