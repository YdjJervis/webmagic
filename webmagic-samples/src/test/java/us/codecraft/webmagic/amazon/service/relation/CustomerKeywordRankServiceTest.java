package us.codecraft.webmagic.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerKeywordRank;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerKeywordRankService;

import java.net.URLEncoder;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/28 13:53
 */
public class CustomerKeywordRankServiceTest extends SpringTestCase {
    @Autowired
    CustomerKeywordRankService mCustomerKeywordRankService;

    @Test
    public void add() {
        CustomerKeywordRank customerKeywordRank = new CustomerKeywordRank();
        customerKeywordRank.setAsin("B00IA530JA");
        customerKeywordRank.setKeyword(URLEncoder.encode("Robinet"));
        customerKeywordRank.setCustomerCode("EC_001");
        customerKeywordRank.setSiteCode("FR");
        customerKeywordRank.setDepartmentCode("search-alias=aps");
        customerKeywordRank.setCrawl(1);
        customerKeywordRank.setPriority(0);
        mCustomerKeywordRankService.add(customerKeywordRank);
    }
}