package com.eccang.spider.amazon.pojo.top100;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/2/21 17:19
 */
public class StockUrl extends BasePojo {

    public String urlMD5;
    public String batchNum;
    public String siteCode;
    public int type;
    public String url;
    public String pUrl;
    public String asin;
    public int status;
    public int crawling;
    public String cookie;
    public int times;

    @Override
    public String toString() {
        return "StockUrl{" +
                "urlMD5='" + urlMD5 + '\'' +
                ", batchNum='" + batchNum + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", pUrl='" + pUrl + '\'' +
                ", asin='" + asin + '\'' +
                ", status=" + status +
                ", crawling=" + crawling +
                ", cookie='" + cookie + '\'' +
                ", times=" + times +
                '}';
    }
}