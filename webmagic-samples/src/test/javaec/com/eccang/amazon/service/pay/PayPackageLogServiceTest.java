package com.eccang.amazon.service.pay;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.pay.PayPackageLog;
import com.eccang.spider.amazon.service.pay.PayPackageLogService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PayPackageLogServiceTest extends SpringTestCase {

    @Autowired
    private PayPackageLogService mService;

    @Test
    public void add() {
        PayPackageLog log = new PayPackageLog();
        log.curd = PayPackageLog.Curd.DELETE;
        log.describe = "删除一个套餐";
        log.customerCode = "EC001";

        mService.add(log);
    }

    @Test
    public void findByPayPackage() {
        System.out.println(mService.findByCusCode("EC001"));
    }
}
