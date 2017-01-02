package com.eccang.spider.amazon.pojo;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 爬取队列实体
 * @date 2016/10/11
 */
public class Url extends BasePojo {

    /**
     * 当前url的16位MD5串
     */
    public String urlMD5;

    public String batchNum;
    /**
     * 抓取类型。0-抓Review
     */
    public int type;
    public String siteCode;
    /**
     * ASIN码
     */
    public String asin;
    /**
     * ReviewId
     */
    public String reviewId;
    /**
     * 爬取URL返回的状态码
     */
    public int status;
    /**
     * 是否在爬取当中。0-否；1-是
     */
    public int crawling;
    /**
     * 爬取优先级
     */
    public int priority;

    /**
     * 爬取的次数
     */
    public int times;

    /**
     * 当前爬取的URL
     */
    public String url;
    /**
     * 当前爬取URL的父级URL
     */
    public String parentUrl;

    @Override
    public String toString() {
        return "Url{" +
                "urlMD5='" + urlMD5 + '\'' +
                ", batchNum='" + batchNum + '\'' +
                ", type=" + type +
                ", siteCode='" + siteCode + '\'' +
                ", asin='" + asin + '\'' +
                ", reviewId='" + reviewId + '\'' +
                ", status=" + status +
                ", crawling=" + crawling +
                ", priority=" + priority +
                ", times=" + times +
                ", url='" + url + '\'' +
                ", parentUrl='" + parentUrl + '\'' +
                '}';
    }
}
