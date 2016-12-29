package us.codecraft.webmagic.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerRankKeyword;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerRankKeywordService;

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
        customerRankKeyword.setAsin("B00NK332BS");
        customerRankKeyword.setKeyword("food");
        customerRankKeyword.setCustomerCode("EC_001");
        customerRankKeyword.setSiteCode("US");
        customerRankKeyword.setDepartmentCode("search-alias=pets");
        customerRankKeyword.setCrawl(1);
        customerRankKeyword.setPriority(2);
        mCustomerRankKeywordService.add(customerRankKeyword);
    }
}