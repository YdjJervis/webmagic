package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.KeywordRankStat;
import us.codecraft.webmagic.samples.amazon.service.KeywordRankStatService;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/2 18:12
 */
public class KeywordRankStatServiceTest extends SpringTestCase {

    @Autowired
    KeywordRankStatService mKeywordRankStatService;

    @Test
    public void add() {
        KeywordRankStat keywordRankStat = new KeywordRankStat();
        keywordRankStat.setRankSearchId(2);

        mKeywordRankStatService.add(keywordRankStat);
        System.out.println(keywordRankStat.getId());
    }

    @Test
    public void deleteById() {
        mKeywordRankStatService.deleteById(1);
    }

    @Test
    public void updateById() {
        KeywordRankStat keywordRankStat = new KeywordRankStat();
        mKeywordRankStatService.updateById(keywordRankStat);
    }

    @Test
    public void findById() {
        mKeywordRankStatService.findById(1);
    }

    @Test
    public void findAll() {
        List<KeywordRankStat> keywordRankStatList = mKeywordRankStatService.findAll();
        System.out.println(keywordRankStatList);
    }

    @Test
    public void findByRankSearchId() {
        KeywordRankStat keywordRankStat = mKeywordRankStatService.findByRankSearchId(1);
        System.out.println(keywordRankStat);
    }
}