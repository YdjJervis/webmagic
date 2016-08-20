package us.codecraft.webmagic.netsense.stats.gov.pojo;

import us.codecraft.webmagic.netsense.base.pojo.BaseBean;

/**
 * 表格数据
 */
public class StatsGov extends BaseBean {

    private String dbcode;
    private String code;
    private String data;
    private boolean hasdata;
    private String dotcount;
    private String url;

    public String getDbcode() {
        return dbcode;
    }

    public void setDbcode(String dbcode) {
        this.dbcode = dbcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isHasdata() {
        return hasdata;
    }

    public void setHasdata(boolean hasdata) {
        this.hasdata = hasdata;
    }

    public String getDotcount() {
        return dotcount;
    }

    public void setDotcount(String dotcount) {
        this.dotcount = dotcount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "StatsGov{" +
                "code='" + code + '\'' +
                ", data='" + data + '\'' +
                ", hasdata=" + hasdata +
                ", dotcount='" + dotcount + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
