package com.eccang.spider.amazon.pojo.relation;

import com.eccang.spider.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户-Asin关系
 * @date 2016/11/23 9:33
 */
public class CustomerAsin extends BasePojo {

    public String customerCode;
    public String siteCode;
    public int immediate;
    public String asin;
    public int crawl = 1;
    public int onSell = 1;
    public int priority;
    public int frequency;
    public String star;
    public Date syncTime;

    public CustomerAsin() {
    }

    public CustomerAsin(String customerCode, String siteCode, String asin, int immediate) {
        this.siteCode = siteCode;
        this.customerCode = customerCode;
        this.asin = asin;
        this.immediate = immediate;
    }

    @Override
    public String toString() {
        return "CustomerAsin{" +
                "customerCode='" + customerCode + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", immediate=" + immediate +
                ", asin='" + asin + '\'' +
                ", crawl=" + crawl +
                ", onSell=" + onSell +
                ", priority=" + priority +
                ", frequency=" + frequency +
                ", star='" + star + '\'' +
                ", syncTime=" + syncTime +
                '}';
    }
}