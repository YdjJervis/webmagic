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
        payPackage.code = 1;
        payPackage.stubCode = 1;
        mService.add(payPackage);
    }

    @Test
    public void find(){
        System.out.println(mService.findByCode(1));
    }


}
