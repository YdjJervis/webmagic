package com.eccang.spider.amazon.dao;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.KeywordRankStat;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * 2016/12/2 17:40
 */
@Repository
public interface KeywordRankStatDao {

    void add(KeywordRankStat keywordRankStat);

    void deleteById(int id);

    void updateById(KeywordRankStat keywordRankStat);

    KeywordRankStat findById(int id);

    List<KeywordRankStat> findAll();

    KeywordRankStat findByRankSearchId(int rankSearchId);
}
