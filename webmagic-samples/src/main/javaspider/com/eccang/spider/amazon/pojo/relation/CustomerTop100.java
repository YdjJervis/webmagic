package com.eccang.spider.amazon.pojo.relation;

import com.eccang.spider.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.2
 * 2017/2/9 15:33
 */
public class CustomerTop100 extends BasePojo {

    public String siteCode;
    public String customerCode;
    public int crawl;
    public int frequency;
    public Date syncTime;

    @Override
    public String toString() {
        return "BatchTop100{" +
                "siteCode='" + siteCode + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", crawl=" + crawl +
                ", frequency=" + frequency +
                ", syncTime=" + syncTime +
                '}';
    }
}