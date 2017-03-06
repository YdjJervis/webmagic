package com.eccang.amazon.service.relation;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;

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
        list.add(initCustomerAsin("US","B01M0MN61Y"));
        list.add(initCustomerAsin("US","B01AMZLDJO"));

        mService.addAll(list);
    }

    @Test
    public void findNeedGenerateBatch(){
        System.out.println(mService.findNeedGenerateBatch());
    }

}
