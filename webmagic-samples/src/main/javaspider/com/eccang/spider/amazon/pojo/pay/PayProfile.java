package com.eccang.spider.amazon.pojo.pay;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 收费基础配置
 * @date 2017/1/16 14:57
 */
public class PayProfile extends BasePojo {

    public String businessCode;
    public float urlPrice;
    public float priorityMutiple;
    public float customMutiple;

    @Override
    public String toString() {
        return "PayProfile{" +
                "businessCode='" + businessCode + '\'' +
                ", urlPrice=" + urlPrice +
                ", priorityMutiple=" + priorityMutiple +
                ", customMutiple=" + customMutiple +
                '}';
    }

}
