package us.codecraft.webmagic.netsense.tianyan.pojo;

import us.codecraft.webmagic.netsense.base.BaseBean;

/**
 * 投资关系
 */
public class RelationShip extends BaseBean {

    private String srcCompany;//投资公司
    private String desCompany;//被投资公司
    private String found;//投资资金
    private String desUrl;
    private String url;

    public String getSrcCompany() {
        return srcCompany;
    }

    public void setSrcCompany(String srcCompany) {
        this.srcCompany = srcCompany;
    }

    public String getDesCompany() {
        return desCompany;
    }

    public void setDesCompany(String desCompany) {
        this.desCompany = desCompany;
    }

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
    }

    public String getDesUrl() {
        return desUrl;
    }

    public void setDesUrl(String desUrl) {
        this.desUrl = desUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RelationShip{" +
                "srcCompany='" + srcCompany + '\'' +
                ", desCompany='" + desCompany + '\'' +
                ", found='" + found + '\'' +
                ", desUrl='" + desUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
