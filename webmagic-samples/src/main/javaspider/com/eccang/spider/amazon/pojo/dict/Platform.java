package com.eccang.spider.amazon.pojo.dict;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 平台表实体。erp，wms等
 * @date 2016/11/8 17:25
 */
public class Platform extends BasePojo {

    public String code;
    public String name;
    public int status;
    public String introduce;

    @Override
    public String toString() {
        return "Platform{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", introduce='" + introduce + '\'' +
                '}';
    }
}