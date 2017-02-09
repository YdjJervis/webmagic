package com.eccang.amazon.service.pay;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.pay.PayPackage;
import com.eccang.spider.amazon.service.pay.PayPackageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PayPackageServiceTest extends SpringTestCase {

    @Autowired
    private PayPackageService mService;

    @Test
    public void add() {
        PayPackage payPackage = new PayPackage();
        payPackage.code = "P002";
        payPackage.status = 0;
        payPackage.custom = 1;
        mService.add(payPackage);
    }

    @Test
    public void findByCode() {
        System.out.println(mService.findByCode("P002"));
    }

    @Test
    public void update() {
        PayPackage payPackage = mService.findByCode("P002");
        payPackage.status = 1;
        payPackage.custom = 0;
        mService.update(payPackage);
    }

    @Test
    public void findBuildIn(){
        System.out.println(mService.findBuildIn());
    }


}
