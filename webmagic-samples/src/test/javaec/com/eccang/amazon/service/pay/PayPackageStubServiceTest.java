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
        stub.code = 1;
        stub.priority = 3;
        stub.count = 300;
        stub.frequency = 3;
        stub.custom=1;
        stub.averageTime = 300;
        stub.price = 300;
        stub.businessCode = "AS";

        list.add(stub);

        mService.addAll(list);
    }

    @Test
    public void findByCode(){
        System.out.println(mService.findByCode(1));
    }

}
