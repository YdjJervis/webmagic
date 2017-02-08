package com.eccang.amazon.service.relation;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerBusinessServiceTest extends SpringTestCase {

    @Autowired
    private CustomerBusinessService mService;


    @Test
    public void add(){
        CustomerBusiness cusBusiness = new CustomerBusiness();
        cusBusiness.customerCode = "Cus001";
        cusBusiness.businessCode = "AS";
        cusBusiness.maxData = 888;
        mService.add(cusBusiness);
    }

}
