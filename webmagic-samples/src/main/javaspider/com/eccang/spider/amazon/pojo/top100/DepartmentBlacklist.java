package com.eccang.spider.amazon.pojo.top100;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/3/15 10:51
 */
public class DepartmentBlacklist extends BasePojo {
    public String depName;
    public String siteCode;
    public String depCode;
    public Integer enable; /*是否启用；1-是，2-否*/

    @Override
    public String toString() {
        return "DepartmentBlacklist{" +
                "depName='" + depName + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", depCode='" + depCode + '\'' +
                ", enable=" + enable +
                '}';
    }
}