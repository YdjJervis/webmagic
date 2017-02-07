package com.eccang.amazon.service.pay;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.amazon.service.pay.PayCalculatorImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/2/7 10:54
 */
public class PayCalculatorTest extends SpringTestCase {

    @Autowired
    private PayCalculatorImpl mPayCalculator;

    @Test
    public void calculate() {
        PayPackageStub payPackageStub = new PayPackageStub();
        payPackageStub.custom = 0;
        payPackageStub.frequency = 24;
        payPackageStub.priority = 4;
        payPackageStub.businessCode = "MS";
        payPackageStub.count = 50;
        payPackageStub.day = 30;

        System.out.println(mPayCalculator.calculate(payPackageStub));
    }
}
