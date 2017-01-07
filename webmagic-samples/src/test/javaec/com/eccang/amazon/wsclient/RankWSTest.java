package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.BaseReqParam;
import com.eccang.pojo.rank.CusRankReq;
import com.eccang.pojo.rank.CusRankUpdateReq;
import com.eccang.pojo.rank.RankQueryReq;
import com.eccang.spider.amazon.service.relation.CustomerKeywordRankService;
import com.eccang.wsclient.rank.RankWSService;
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
        keywordRank.setAsin("B001S2PKPE");
        keywordRank.setSiteCode("US");
        keywordRank.setKeyword("phone");
        keywordRank.setDepartmentCode("search-alias=aps");
        keywordRank.setFrequency(2);
        keywordRanks.add(keywordRank);

        /* 添加第二个数据 */
        keywordRank = cusRankReq.new KeywordRank();

        keywordRank.setAsin("B01ARQ39CW");
        keywordRank.setSiteCode("US");
        keywordRank.setKeyword("water tap");
        keywordRank.setDepartmentCode("search-alias=aps");
        keywordRank.setFrequency(2);
        keywordRanks.add(keywordRank);

        cusRankReq.setData(keywordRanks);

        String json = new RankWSService().getRankWSPort().addToMonitor(new Gson().toJson(cusRankReq));
        System.out.println(json);
    }

    @Test
    public void setStatus() {
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

        String json = new RankWSService().getRankWSPort().setStatus(new Gson().toJson(cusRankUpdateReq));
        System.out.println(json);
    }

    @Test
    public void getKeywordRankInfo() {
        RankQueryReq rankQueryReq = new RankQueryReq();
        RankQueryReq.KeywordInfo keywordInfo = rankQueryReq.new KeywordInfo();
        rankQueryReq.customerCode = "EC_001";
        rankQueryReq.platformCode = "ERP";
        rankQueryReq.token = "123456789";

        keywordInfo.setBatchNum("EC2017010710150064");
        keywordInfo.setAsin("B001S2PKPE");
        keywordInfo.setSiteCode("US");
        keywordInfo.setDepartmentCode("search-alias=aps");
        keywordInfo.setKeyword("phone");

        rankQueryReq.setData(keywordInfo);

        String json = new RankWSService().getRankWSPort().getKeywordRankInfo(new Gson().toJson(rankQueryReq));
        System.out.println(json);
    }

    @Test
    public void getMonitorList() {
        BaseReqParam baseReqParam = new BaseReqParam();
        baseReqParam.customerCode = "EC_001";
        baseReqParam.platformCode = "ERP";
        baseReqParam.token = "123456789";

        String json = new RankWSService().getRankWSPort().getMonitorList(new Gson().toJson(baseReqParam));
        System.out.println(json);
    }

}