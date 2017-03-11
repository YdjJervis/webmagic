package com.eccang.spider.amazon.pojo.dict;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description:
 * @date 2016/11/10 13:59
 */
public class API extends BasePojo {

    public String customerCode;
    public int status;
    public String token;

    public Customer customer;
    public String pushUrl;

    @Override
    public String toString() {
        return "API{" +
                "customerCode='" + customerCode + '\'' +
                ", status=" + status +
                ", token='" + token + '\'' +
                ", customer=" + customer +
                ", pushUrl='" + pushUrl + '\'' +
                '}';
    }
}