package us.codecraft.webmagic.netsense.tianyan.pojo;

import us.codecraft.webmagic.netsense.base.BaseBean;

/**
 * 公司信息
 */
public class CompanyCvs extends BaseBean {

    private String name;//公司名
    private String orgCode;//组织机构代码
    private String certificate;//资质证书
    private String issueUnit;//发证单位

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getIssueUnit() {
        return issueUnit;
    }

    public void setIssueUnit(String issueUnit) {
        this.issueUnit = issueUnit;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", certificate='" + certificate + '\'' +
                ", issueUnit='" + issueUnit + '\'' +
                '}';
    }
}
