package us.codecraft.webmagic.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.CustomerKeywordRankReq;
import com.eccang.pojo.CustomerKeywordRankUpdateReq;
import com.eccang.spider.amazon.service.relation.CustomerKeywordRankService;
import com.eccang.wsclient.keywordRank.KeywordRankWSService;
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
public class KeywordRankWSTest extends SpringTestCase {

    @Autowired
    CustomerKeywordRankService mCustomerKeywordRankService;

    @Test
    public void addToMonitor() {
        CustomerKeywordRankReq customerKeywordRankReq = new CustomerKeywordRankReq();
        List<CustomerKeywordRankReq.KeywordRank> keywordRanks = new ArrayList<>();
        CustomerKeywordRankReq.KeywordRank keywordRank = customerKeywordRankReq.new KeywordRank();
        customerKeywordRankReq.cutomerCode = "EC_001";
        customerKeywordRankReq.platformCode = "ERP";
        customerKeywordRankReq.token = "123456789";
        keywordRank.setAsin("0596009208");
        keywordRank.setSiteCode("US");
        keywordRank.setKeyword("java");
        keywordRank.setFrequency(2);
        keywordRank.setPriority(0);
        keywordRank.setDepartmentCode("search-alias=aps");
        keywordRanks.add(keywordRank);
        customerKeywordRankReq.setData(keywordRanks);

        String json = new KeywordRankWSService().getKeywordRankWSPort().addToMonitor(new Gson().toJson(customerKeywordRankReq));
        System.out.println(json);
    }

    @Test
    public void setStat() {
        CustomerKeywordRankUpdateReq customerKeywordRankUpdateReq = new CustomerKeywordRankUpdateReq();
        List<CustomerKeywordRankUpdateReq.KeywordRank> keywordRanks = new ArrayList<>();
        CustomerKeywordRankUpdateReq.KeywordRank keywordRank = customerKeywordRankUpdateReq.new KeywordRank();
        customerKeywordRankUpdateReq.cutomerCode = "EC_001";
        customerKeywordRankUpdateReq.platformCode = "ERP";
        customerKeywordRankUpdateReq.token = "123456789";
        keywordRank.setAsin("0596009208");
        keywordRank.setSiteCode("US");
        keywordRank.setKeyword("java");
        keywordRank.setFrequency(4);
        keywordRank.setPriority(2);
        keywordRank.setCrawl(1);
        keywordRank.setDepartmentCode("search-alias=aps");
        keywordRanks.add(keywordRank);
        customerKeywordRankUpdateReq.setData(keywordRanks);

        String json = new KeywordRankWSService().getKeywordRankWSPort().setStatus(new Gson().toJson(customerKeywordRankUpdateReq));
        System.out.println(json);
    }
}