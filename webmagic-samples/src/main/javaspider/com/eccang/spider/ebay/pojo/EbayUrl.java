package com.eccang.spider.ebay.pojo;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/16 16:30
 */
public class EbayUrl extends BasePojo {
    public String url;
    public String categoryName;
    public int type;
    public int status;
    public int crawling;

    @Override
    public String toString() {
        return "Url{" +
                "url='" + url + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", type=" + type + '\'' +
                ", crawling=" + crawling + '\'' +
                ", status=" + status +
                '}';
    }
}