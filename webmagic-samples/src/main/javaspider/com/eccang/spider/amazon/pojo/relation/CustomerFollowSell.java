package com.eccang.spider.amazon.pojo.relation;

import com.eccang.spider.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 客户和跟卖关系实体
 * @date 2016/12/26 15:42
 */
public class CustomerFollowSell extends BasePojo {

    public String customerCode;
    public String siteCode;
    public String asin;
    public String sellerId;
    public int crawl = 1;
    public int onSell = 1;
    public int priority;
    public int frequency;
    public int times;
    public Date syncTime;

    @Override
    public String toString() {
        return "CustomerFollowSell{" +
                "customerCode='" + customerCode + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", asin='" + asin + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", crawl=" + crawl +
                ", onSell=" + onSell +
                ", priority=" + priority +
                ", frequency=" + frequency +
                ", times=" + times +
                ", syncTime=" + syncTime +
                '}';
    }
}
