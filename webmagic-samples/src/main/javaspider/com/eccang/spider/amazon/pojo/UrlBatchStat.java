package com.eccang.spider.amazon.pojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.1
 * 对每个批次下的URL请求状态监测实体类
 * 2016/11/16 16:53
 */
public class UrlBatchStat {

    private int id;
    private String url;
    private String batchNum; /*批次号*/
    private int isProxy; /*是否使用代理*/
    private int correctTime; /*请求正常次数*/
    private int exceptionTime; /*请求异常次数*/
    private Date createDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public int getIsProxy() {
        return isProxy;
    }

    public void setIsProxy(int isProxy) {
        this.isProxy = isProxy;
    }

    public int getCorrectTime() {
        return correctTime;
    }

    public void setCorrectTime(int correctTime) {
        this.correctTime = correctTime;
    }

    public int getExceptionTime() {
        return exceptionTime;
    }

    public void setExceptionTime(int exceptionTime) {
        this.exceptionTime = exceptionTime;
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