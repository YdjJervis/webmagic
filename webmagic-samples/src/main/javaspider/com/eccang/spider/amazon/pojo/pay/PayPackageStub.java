package com.eccang.spider.amazon.pojo.pay;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/1/16 15:44
 */
public class PayPackageStub extends BasePojo {

    public String stubCode;
    public String payPackageCode;
    public String businessCode;
    public int day;
    public int priority;
    public int frequency;
    public int count;
    public int averageTime;
    public float price;

    @Override
    public String toString() {
        return "PayPackageStub{" +
                "stubCode='" + stubCode + '\'' +
                ", payPackageCode='" + payPackageCode + '\'' +
                ", businessCode='" + businessCode + '\'' +
                ", day=" + day +
                ", priority=" + priority +
                ", frequency=" + frequency +
                ", count=" + count +
                ", averageTime=" + averageTime +
                ", price=" + price +
                '}';
    }
}
