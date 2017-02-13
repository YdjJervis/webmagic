package com.eccang.amazon.service.pay;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.amazon.service.pay.PayPackageStubService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class PayPackageStubServiceTest extends SpringTestCase {

    @Autowired
    private PayPackageStubService mService;

    @Test
    public void add() {
        List<PayPackageStub> list = new ArrayList<>();

        PayPackageStub stub = new PayPackageStub();
        stub.stubCode = "PS001";
        stub.payPackageCode = "P002";
        stub.priority = 3;
        stub.count = 300;
        stub.frequency = 3;
        stub.averageTime = 300;
        stub.price = 300;
        stub.businessCode = "AS";
        stub.day = 30;

        list.add(stub);

        mService.addAll(list);
    }

    @Test
    public void findByCode() {
        System.out.println(mService.findByCode("PS001"));
    }

    @Test
    public void findByPayPackage() {
        System.out.println(mService.findByPayPackage("f1b014"));
    }

    @Test
    public void findTotalPrice(){
        System.out.println(mService.findTotalPrice("f1b014"));
    }
}
