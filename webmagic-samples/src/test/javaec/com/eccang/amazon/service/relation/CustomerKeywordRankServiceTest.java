package com.eccang.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.relation.CustomerKeywordRank;
import com.eccang.spider.amazon.service.relation.CustomerKeywordRankService;

import java.net.URLEncoder;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/28 13:53
 */
public class CustomerKeywordRankServiceTest extends SpringTestCase {
    @Autowired
    private CustomerKeywordRankService mCustomerKeywordRankService;

    @Test
    public void add() {
        CustomerKeywordRank customerKeywordRank = new CustomerKeywordRank();
        customerKeywordRank.setAsin("1338099132");
        customerKeywordRank.setKeyword(URLEncoder.encode("book"));
        customerKeywordRank.setCustomerCode("EC_001");
        customerKeywordRank.setSiteCode("US");
        customerKeywordRank.setDepartmentCode("search-alias=aps");
        customerKeywordRank.setCrawl(1);
        customerKeywordRank.setPriority(0);
        mCustomerKeywordRankService.add(customerKeywordRank);
    }
}