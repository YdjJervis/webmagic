package us.codecraft.webmagic.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerFollowSell;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerFollowSellService;

public class CustomerFollowSellServiceTest extends SpringTestCase {

    @Autowired
    private CustomerFollowSellService mService;

    @Test
    public void addOne() {
        CustomerFollowSell customerFollowSell = new CustomerFollowSell();
        customerFollowSell.customerCode = "Customer001";
        customerFollowSell.siteCode = "US";
        customerFollowSell.asin = "Asin001";
        customerFollowSell.frequency = 2;
        mService.add(customerFollowSell);
    }
}
