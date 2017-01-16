package com.eccang.spider.amazon.pojo.pay;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 客户对于收费抓取每一个业务的参数可配置项
 * @date 2017/1/16 15:23
 */
public class PayPackage extends BasePojo {

    public int code;
    public int status = 1;
    public int stubCode;

    @Override
    public String toString() {
        return "PayPackage{" +
                "code='" + code + '\'' +
                ", status=" + status +
                ", stubCode=" + stubCode +
                '}';
    }
}
