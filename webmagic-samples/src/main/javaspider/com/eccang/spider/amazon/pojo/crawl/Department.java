package com.eccang.spider.amazon.pojo.crawl;

import com.eccang.spider.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.2
 * 品类信息对象
 * 2017/1/16 10:18
 */
public class Department extends BasePojo {

    public String depName;
    public String parentDepName;
    public String depTab;
    public String depUrl;
    public int depLevel;
    public Date syncTime;

    @Override
    public String toString() {
        return "Department{" +
                "depName='" + depName + '\'' +
                ", parentDepName='" + parentDepName + '\'' +
                ", depTab='" + depTab + '\'' +
                ", depUrl='" + depUrl + '\'' +
                ", depLevel=" + depLevel + '\'' +
                ", syncTime=" + syncTime +
                '}';
    }
}