package com.eccang.spider.amazon.pojo.dict;

import com.eccang.spider.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/15 15:41
 */
public class ProxyAgencyBaseInfo extends BasePojo {

    private int id;
    private String proxyCode;
    private Date createDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProxyCode() {
        return proxyCode;
    }

    public void setProxyCode(String proxyCode) {
        this.proxyCode = proxyCode;
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