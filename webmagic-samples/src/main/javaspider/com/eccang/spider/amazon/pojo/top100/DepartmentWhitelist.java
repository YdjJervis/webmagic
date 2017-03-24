package com.eccang.spider.amazon.pojo.top100;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/3/15 15:36
 */
public class DepartmentWhitelist extends BasePojo {

    public String depName;
    public String siteCode;
    public String depCode;
    public int priority;
    public int enable;

    @Override
    public String toString() {
        return "DepartmentWhitelist{" +
                "depName='" + depName + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", depCode='" + depCode + '\'' +
                ", priority=" + priority +
                ", enable=" + enable +
                '}';
    }
}