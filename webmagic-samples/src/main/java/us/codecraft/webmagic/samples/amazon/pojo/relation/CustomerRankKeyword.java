package us.codecraft.webmagic.samples.amazon.pojo.relation;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.2
 * @Description: 搜索关键词与客户关系
 * @date 2016/12/26 14:05
 */
public class CustomerRankKeyword {
    private int id;
    private String asin;
    private String keyword;
    private String customerCode;
    private String siteCode; /*站点码*/
    private String departmentCode; /*品类码*/
    private int crawl; /*是否开启*/
    private int priority;
    private int frequency; /*执行频率*/
    private Date syncTime; /*同步时间*/
    private Date createTime;
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public int getCrawl() {
        return crawl;
    }

    public void setCrawl(int crawl) {
        this.crawl = crawl;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Date getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Date syncTime) {
        this.syncTime = syncTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}