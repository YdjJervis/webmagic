package com.eccang.spider.amazon.pojo;

import com.eccang.spider.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.2
 *          爬虫业务功能实体类
 *          2016/12/15 16:02
 */
public class Business extends BasePojo {
    private int id;
    private String businessCode;
    private String businessName;
    private int status;
    private int importLimit;
    private Date createDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    public int getImportLimit() {
        return importLimit;
    }

    public void setImportLimit(int importLimit) {
        this.importLimit = importLimit;
    }

    @Override
    public String toString() {
        return "Business{" +
                "id=" + id +
                ", businessCode='" + businessCode + '\'' +
                ", businessName='" + businessName + '\'' +
                ", status=" + status +
                ", importLimit=" + importLimit +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                '}';
    }
}