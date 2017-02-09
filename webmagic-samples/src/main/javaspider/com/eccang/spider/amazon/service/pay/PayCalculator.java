package com.eccang.spider.amazon.service.pay;

import com.eccang.spider.amazon.pojo.pay.PayPackageStub;

import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 支付套餐计算器
 * @date 2017/2/6 18:18
 */
public interface PayCalculator {

    /**
     * @param payPackageStubList 套餐子业务列表
     * @param custom 1-是自定义套餐/0-不是
     * @return 保留整十数的套餐价格
     */
    int calculate(List<PayPackageStub> payPackageStubList,int custom);

    /**
     * @param payPackageStub 套餐子业务
     * @param custom 1-是自定义套餐/0-不是
     * @return 保留整十数的套餐价格
     */
    int calculate(PayPackageStub payPackageStub,int custom);
}
