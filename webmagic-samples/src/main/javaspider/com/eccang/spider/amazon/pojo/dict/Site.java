package com.eccang.spider.amazon.pojo.dict;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 网页字典表
 * @date 2016/10/11
 */
public class Site extends BasePojo {

    public String code;
    public String site;
    public String name;
    /**
     * 站点是否爬取。0-不爬取；1-要爬取
     */
    public int crawl;

    @Override
    public String toString() {
        return "Site{" +
                "code='" + code + '\'' +
                ", site='" + site + '\'' +
                ", name='" + name + '\'' +
                ", crawl=" + crawl +
                '}';
    }
}
