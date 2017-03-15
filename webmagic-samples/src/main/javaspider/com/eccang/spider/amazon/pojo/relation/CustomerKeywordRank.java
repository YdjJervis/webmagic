package com.eccang.spider.amazon.pojo.relation;

import com.eccang.spider.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.2
 * @Description: 搜索关键词与客户关系
 * @date 2016/12/26 14:05
 */
public class CustomerKeywordRank extends BasePojo{

    public String asin;
    public String keyword;
    public String customerCode;
    public String siteCode; /*站点码*/
    public String departmentCode; /*品类码*/
    public int immediate; /*品类码*/
    public int crawl; /*是否开启*/
    public int priority;
    public int frequency; /*执行频率*/
    public Date syncTime; /*同步时间*/

    @Override
    public String toString() {
        return "CustomerKeywordRank{" +
                "asin='" + asin + '\'' +
                ", keyword='" + keyword + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", departmentCode='" + departmentCode + '\'' +
                ", immediate=" + immediate +
                ", crawl=" + crawl +
                ", priority=" + priority +
                ", frequency=" + frequency +
                ", syncTime=" + syncTime +
                '}';
    }
}