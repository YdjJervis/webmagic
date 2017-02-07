package com.eccang.spider.amazon.pojo.relation;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 客户跟收费套餐的关系
 * @date 2017/1/16 15:53
 */
public class CustomerPayPackage extends BasePojo{

    public String customerCode;
    public String packageCode;
    public int status;

    @Override
    public String toString() {
        return "CustomerPayPackage{" +
                "customerCode='" + customerCode + '\'' +
                ", packageCode='" + packageCode + '\'' +
                ", status=" + status +
                '}';
    }
}
