package com.eccang.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.RankSearchKeyword;
import com.eccang.spider.amazon.service.RankSearchKeywordService;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/2 17:02
 */
public class RankSearchKeywordServiceTest extends SpringTestCase {

    @Autowired
    RankSearchKeywordService mRankSearchKeywordService;

    /**
     * 添加一条数据
     */
    @Test
    public void addTest() {
        RankSearchKeyword rankSearchKeyword = new RankSearchKeyword();
        rankSearchKeyword.setAsin("B008AE5RKG");
        rankSearchKeyword.setKeyword("watch black");
        rankSearchKeyword.setCustomer("EC_C");
        rankSearchKeyword.setSiteCode("https://www.amazon.com");
        mRankSearchKeywordService.add(rankSearchKeyword);
    }

    /**
     * 通过id删除一条数据
     */
    @Test
    public void deleteByIdTest() {
        mRankSearchKeywordService.deleteById(1);
    }

    /**
     * 通过id更新一条数据
     */
    @Test
    public void updateByIdTest() {
        RankSearchKeyword rankSearchKeyword = new RankSearchKeyword();
        mRankSearchKeywordService.updateById(rankSearchKeyword);
    }

    /**
     * 通过id查询一条数据
     */
    @Test
    public void findByIdTest() {
        RankSearchKeyword rankSearchKeyword = mRankSearchKeywordService.findById(2);
        System.out.println(rankSearchKeyword);
    }

    /**
     * 查询所有数据
     */
    @Test
    public void findAllTest() {
        List<RankSearchKeyword> rankSearchKeywordList = mRankSearchKeywordService.findAll();
        System.out.println(rankSearchKeywordList);
    }
}