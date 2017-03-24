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

    public String batchNum;
    public String depName;
    public Integer pId;
    public String parentDepName;
    public String siteCode;
    public String depCode;
    public String depTab;
    public String depUrl;
    public int depLevel;
    public Date syncTime;

    @Override
    public String toString() {
        return "Department{" +
                "batchNum='" + batchNum + '\'' +
                ", depName='" + depName + '\'' +
                ", pId =" + pId +
                ", parentDepName='" + parentDepName + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", depCode='" + depCode + '\'' +
                ", depTab='" + depTab + '\'' +
                ", depUrl='" + depUrl + '\'' +
                ", depLevel=" + depLevel + '\'' +
                ", syncTime=" + syncTime +
                '}';
    }
}