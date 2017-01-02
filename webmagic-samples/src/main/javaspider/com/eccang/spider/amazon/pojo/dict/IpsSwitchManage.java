package com.eccang.spider.amazon.pojo.dict;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/31 10:09
 */
public class IpsSwitchManage {

    private int id;
    private String ipsType;
    private int totalNum;
    private int isComplete;
    private String exceptionInfo;
    private Date createDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIpsType() {
        return ipsType;
    }

    public void setIpsType(String ipsType) {
        this.ipsType = ipsType;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
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