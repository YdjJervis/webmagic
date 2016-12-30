package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.KeywordRank;
import us.codecraft.webmagic.samples.amazon.service.crawl.KeywordRankService;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/28 16:45
 */
public class KeywordRankServiceTest extends SpringTestCase{
    @Autowired
    KeywordRankService mKeywordRankService;

    @Test
    public void findByObj() {
        KeywordRank keywordRank = new KeywordRank();
        keywordRank.setAsin("B01L7BDLL0");
        keywordRank.setKeyword("watch");
        keywordRank.setSiteCode("US");
        keywordRank.setDepartmentCode("search-alias=aps");
        KeywordRank rank = mKeywordRankService.findByObj(keywordRank);
        System.out.println(rank);
    }

}