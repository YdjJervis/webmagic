package com.eccang.spider.ebay.pojo;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/17 10:40
 */
public class SellerInfo extends BasePojo {
    public String categoryName;
    public String url;
    public String sellerName;
    public String address;
    public String phone;
    public String email;
    public String fax;

    @Override
    public String toString() {
        return "SellerInfo{" +
                "categoryName='" + categoryName + '\'' +
                ", url='" + url + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", fax='" + fax + '\'' +
                '}';
    }
}