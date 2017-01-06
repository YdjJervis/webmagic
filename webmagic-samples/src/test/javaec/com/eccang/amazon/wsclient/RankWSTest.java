package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.rank.CusRankReq;
import com.eccang.pojo.rank.CusRankUpdateReq;
import com.eccang.spider.amazon.service.relation.CustomerKeywordRankService;
import com.eccang.wsclient.rank.KeywordRankWSService;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/2 17:37
 */
public class RankWSTest extends SpringTestCase {

    @Autowired
    CustomerKeywordRankService mCustomerKeywordRankService;

    @Test
    public void addToMonitor() {
        CusRankReq cusRankReq = new CusRankReq();
        List<CusRankReq.KeywordRank> keywordRanks = new ArrayList<>();
        CusRankReq.KeywordRank keywordRank = cusRankReq.new KeywordRank();

        cusRankReq.customerCode = "EC_001";
        cusRankReq.platformCode = "ERP";
        cusRankReq.token = "123456789";

        /* 添加第一个数据 */
        keywordRank.setAsin("B01HPI5AM2");
        keywordRank.setSiteCode("US");
        keywordRank.setKeyword("phone");
        keywordRank.setDepartmentCode("search-alias=aps");

        keywordRanks.add(keywordRank);

        /* 添加第二个数据 */
        keywordRank = cusRankReq.new KeywordRank();

        keywordRank.setAsin("B01MED1SHR");
        keywordRank.setSiteCode("UK");
        keywordRank.setKeyword("phone");
        keywordRank.setDepartmentCode("search-alias=aps");

        keywordRanks.add(keywordRank);

        cusRankReq.setData(keywordRanks);

        String json = new KeywordRankWSService().getKeywordRankWSPort().addToMonitor(new Gson().toJson(cusRankReq));
        System.out.println(json);
    }

    @Test
    public void setStat() {
        CusRankUpdateReq cusRankUpdateReq = new CusRankUpdateReq();
        List<CusRankUpdateReq.KeywordRank> keywordRanks = new ArrayList<>();
        CusRankUpdateReq.KeywordRank keywordRank = cusRankUpdateReq.new KeywordRank();
        cusRankUpdateReq.customerCode = "EC_001";
        cusRankUpdateReq.platformCode = "ERP";
        cusRankUpdateReq.token = "123456789";
        keywordRank.setAsin("0596009208");
        keywordRank.setSiteCode("US");
        keywordRank.setKeyword("java");
        keywordRank.setFrequency(4);
        keywordRank.setPriority(2);
        keywordRank.setCrawl(1);
        keywordRank.setDepartmentCode("search-alias=aps");
        keywordRanks.add(keywordRank);
        cusRankUpdateReq.setData(keywordRanks);

        String json = new KeywordRankWSService().getKeywordRankWSPort().setStatus(new Gson().toJson(cusRankUpdateReq));
        System.out.println(json);
    }
}