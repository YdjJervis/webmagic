package com.eccang.spider.amazon.pojo.relation;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/15 16:30
 */
public class CustomerBusiness extends BasePojo{

    public String customerCode;
    public String businessCode;
    public int maxData;
    public int useData;

    @Override
    public String toString() {
        return "CustomerBusiness{" +
                "customerCode='" + customerCode + '\'' +
                ", businessCode='" + businessCode + '\'' +
                ", maxData=" + maxData +
                ", useData=" + useData +
                '}';
    }
}