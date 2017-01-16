package com.eccang.spider.amazon.pojo.pay;

import com.eccang.spider.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/1/16 15:44
 */
public class PayPackageStub extends BasePojo {

    public String code;
    public int custom;
    public String businessCode;
    public int priority;
    public int frequency;
    public int count;
    public Date averageTime;
    public float price;

    @Override
    public String toString() {
        return "PayPackageStub{" +
                "code='" + code + '\'' +
                ", custom=" + custom +
                ", businessCode='" + businessCode + '\'' +
                ", priority=" + priority +
                ", frequency=" + frequency +
                ", count=" + count +
                ", averageTime=" + averageTime +
                ", price=" + price +
                '}';
    }
}
