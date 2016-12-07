package us.codecraft.webmagic.samples.amazon.pojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/5 13:58
 */
public class RelationCustomerAsin {

    private int id;
    private String customerCode; /*客户码*/
    private String platformCode; /*平台码*/
    private String siteCode; /*站点码*/
    private String asin;
    private String rootAsin;
    private int status; /*是否是开启状态。1-是/0-否*/
    private Date createDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getRootAsin() {
        return rootAsin;
    }

    public void setRootAsin(String rootAsin) {
        this.rootAsin = rootAsin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}