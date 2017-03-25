package com.eccang.spider.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.KeywordRankStatDao;
import com.eccang.spider.amazon.pojo.KeywordRankStat;

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
    private KeywordRankStatDao mKeywordRankStatDao;

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