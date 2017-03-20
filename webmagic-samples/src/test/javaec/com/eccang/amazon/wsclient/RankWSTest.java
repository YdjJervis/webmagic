package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.BaseReqParam;
import com.eccang.pojo.rank.CusRankUpdateReq;
import com.eccang.pojo.rank.RankQueryReq;
import com.eccang.pojo.rank.RankReq;
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
        RankReq rankReq = new RankReq();
        List<RankReq.KeywordRank> keywordRanks = new ArrayList<>();
        RankReq.KeywordRank keywordRank = rankReq.new KeywordRank();

        rankReq.customerCode = "EC_001";
        rankReq.platformCode = "ERP";
        rankReq.token = "123456789";

        /* 添加第一个数据 */
        keywordRank.asin = "B001S2PKPE";
        keywordRank.siteCode = "US";
        keywordRank.keyword = "phone";
        keywordRank.departmentCode = "search-alias=aps";
        keywordRanks.add(keywordRank);

        /* 添加第二个数据 */
//        keywordRank = rankReq.new KeywordRank();
//
//        keywordRank.asin = "B01ARQ39CW";
//        keywordRank.siteCode = "US";
//        keywordRank.keyword = "water tap";
//        keywordRank.departmentCode = "search-alias=aps";
//        keywordRanks.add(keywordRank);

        rankReq.setData(keywordRanks);

        String json = new RankWSService().getRankWSPort().addToMonitor(new Gson().toJson(rankReq));
        System.out.println(json);
    }

    @Test
    public void addToMonitorLoop() {
        RankReq rankReq = new RankReq();
        rankReq.customerCode = "EC_002";
        rankReq.platformCode = "ERP";
        rankReq.token = "123456789";

        List<RankReq.KeywordRank> keywordRanks = new ArrayList<>();

        for (int i = 6; i < 7; i++) {
            RankReq.KeywordRank keywordRank = rankReq.new KeywordRank();
            keywordRank.asin = "B001S2PKPE";
            keywordRank.siteCode = "US";
            keywordRank.keyword = "phone" + i;
            keywordRank.departmentCode = "search-alias=aps";
            keywordRanks.add(keywordRank);
        }
        rankReq.setData(keywordRanks);

        String json = new RankWSService().getRankWSPort().addToMonitor(new Gson().toJson(rankReq));
        System.out.println(json);
    }

    @Test
    public void setStatus() {
        CusRankUpdateReq cusRankUpdateReq = new CusRankUpdateReq();
        cusRankUpdateReq.customerCode = "EC_002";
        cusRankUpdateReq.platformCode = "ERP";
        cusRankUpdateReq.token = "123456789";

        List<CusRankUpdateReq.KeywordRank> keywordRanks = new ArrayList<>();
        CusRankUpdateReq.KeywordRank keywordRank = cusRankUpdateReq.new KeywordRank();
        keywordRank.asin = "B001S2PKPE";
        keywordRank.siteCode = "US";
        keywordRank.keyword = "phone1";
        keywordRank.crawl = 1;
        keywordRank.departmentCode = "search-alias=aps";
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

        keywordInfo.batchNum = "EC2017010710150064";
        keywordInfo.asin = "B001S2PKPE";
        keywordInfo.siteCode = "US";
        keywordInfo.departmentCode = "search-alias=aps";
        keywordInfo.keyword = "phone";

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