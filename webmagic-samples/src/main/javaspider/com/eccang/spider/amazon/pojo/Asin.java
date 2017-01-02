package com.eccang.spider.amazon.pojo;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: ASIN码对象
 * @date 2016/10/11
 */
public class Asin extends BasePojo {

    /**
     * asin码
     */
    public String siteCode;
    public String rootAsin;

    public Asin() {
    }

    public Asin(String siteCode, String rootAsin) {
        this.siteCode = siteCode;
        this.rootAsin = rootAsin;
    }

    @Override
    public String toString() {
        return "Asin{" +
                "siteCode='" + siteCode + '\'' +
                ", rootAsin='" + rootAsin + '\'' +
                '}';
    }
}
