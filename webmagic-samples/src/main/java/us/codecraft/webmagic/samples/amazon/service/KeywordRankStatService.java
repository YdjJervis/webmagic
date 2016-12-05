package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.KeywordRankStatDao;
import us.codecraft.webmagic.samples.amazon.pojo.KeywordRankStat;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/2 18:09
 */
@Service
public class KeywordRankStatService {

    @Autowired
    KeywordRankStatDao mKeywordRankStatDao;

    public void add(KeywordRankStat keywordRankStat) {
        mKeywordRankStatDao.add(keywordRankStat);
    }

    public void deleteById(int id) {
        mKeywordRankStatDao.deleteById(id);
    }

    public void updateById(KeywordRankStat keywordRankStat) {
        mKeywordRankStatDao.updateById(keywordRankStat);
    }

    public KeywordRankStat findById(int id) {
        return mKeywordRankStatDao.findById(id);
    }

    public List<KeywordRankStat> findAll() {
        return mKeywordRankStatDao.findAll();
    }

    public KeywordRankStat findByRankSearchId(int rankSearchId) {
        return mKeywordRankStatDao.findByRankSearchId(rankSearchId);
    }
}