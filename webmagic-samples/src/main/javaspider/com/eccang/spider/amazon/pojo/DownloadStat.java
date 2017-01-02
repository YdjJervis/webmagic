package com.eccang.spider.amazon.pojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/4 15:24
 */
public class DownloadStat {
    private Integer id;
    private String ipsType;
    private String urlMD5;
    private Integer totalTime;
    private Integer validTime;
    private Integer invalidTime;
    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIpsType() {
        return ipsType;
    }

    public void setIpsType(String ipsType) {
        this.ipsType = ipsType;
    }

    public String getUrlMD5() {
        return urlMD5;
    }

    public void setUrlMD5(String urlMD5) {
        this.urlMD5 = urlMD5;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getValidTime() {
        return validTime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}