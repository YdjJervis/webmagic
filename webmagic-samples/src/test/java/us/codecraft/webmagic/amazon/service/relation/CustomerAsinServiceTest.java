package us.codecraft.webmagic.amazon.service.relation;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerAsin;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerAsinService;

import java.util.ArrayList;
import java.util.List;

public class CustomerAsinServiceTest extends SpringTestCase {

    @Autowired
    CustomerAsinService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testFindByCode() {
        System.out.println(mService.findByCustomerCode("system"));
    }

    @Test
    public void testUpdate() {
        CustomerAsin customerAsin = initCustomerAsin("system", "asin001");
        mService.update(customerAsin);
    }

    private CustomerAsin initCustomerAsin(String system, String asin) {
        CustomerAsin customerAsin = new CustomerAsin();
        customerAsin.customerCode = system;
        customerAsin.siteCode = "US";
        customerAsin.asin = asin;
        customerAsin.crawl = 0;
        return customerAsin;
    }

    @Test
    public void testAddAll() {
        List<CustomerAsin> list = new ArrayList<CustomerAsin>();
        list.add(initCustomerAsin("system","asin002"));
        list.add(initCustomerAsin("system","asin003"));

        mService.addAll(list);
    }

}
