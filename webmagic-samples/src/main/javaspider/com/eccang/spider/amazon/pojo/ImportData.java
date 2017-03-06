package com.eccang.spider.amazon.pojo;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/5 20:09
 */
public class ImportData extends BasePojo {

    private String asin;
    private String siteCode;

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }
}