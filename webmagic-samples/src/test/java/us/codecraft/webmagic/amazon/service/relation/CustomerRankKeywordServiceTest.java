package us.codecraft.webmagic.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerRankKeyword;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerRankKeywordService;

import java.net.URLEncoder;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/28 13:53
 */
public class CustomerRankKeywordServiceTest extends SpringTestCase {
    @Autowired
    CustomerRankKeywordService mCustomerRankKeywordService;

    @Test
    public void add() {
        CustomerRankKeyword customerRankKeyword = new CustomerRankKeyword();
        customerRankKeyword.setAsin("B00IA530JA");
        customerRankKeyword.setKeyword(URLEncoder.encode("Robinet"));
        customerRankKeyword.setCustomerCode("EC_001");
        customerRankKeyword.setSiteCode("FR");
        customerRankKeyword.setDepartmentCode("search-alias=aps");
        customerRankKeyword.setCrawl(1);
        customerRankKeyword.setPriority(0);
        mCustomerRankKeywordService.add(customerRankKeyword);
    }
}