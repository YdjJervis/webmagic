package com.eccang.amazon.service.relation;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.relation.CustomerPayPackage;
import com.eccang.spider.amazon.service.relation.CustomerPayPackageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerPayPackageServiceTest extends SpringTestCase {

    @Autowired
    private CustomerPayPackageService mService;


    @Test
    public void add(){
        CustomerPayPackage payPackage = new CustomerPayPackage();
        payPackage.customerCode = "EC_001";
        payPackage.packageCode = "46479c";
        mService.add(payPackage);
    }

    @Test
    public void findByCustomerCode(){
        System.out.println(mService.findByCustomerCode("EC_001"));
    }

}
