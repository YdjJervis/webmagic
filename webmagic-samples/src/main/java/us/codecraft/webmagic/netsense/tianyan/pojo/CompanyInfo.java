package us.codecraft.webmagic.netsense.tianyan.pojo;


import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.netsense.base.BaseBean;

/**
 * 天眼查
 */
public class CompanyInfo extends BaseBean {

    private static final String BASE_PATH = "//div[@class='row b-c-white company-content']";

    @ExtractBy("//div[@class='company_info_text']/p/text()")
    private String name;

    @ExtractBy(BASE_PATH + "/table/tbody/tr[2]/td[1]//a/text()")
    private String legalPerson;

    @ExtractBy(BASE_PATH + "/table/tbody/tr[2]/td[2]/p/text()")
    private String regFund;

    @ExtractBy(BASE_PATH + "/table[1]/tbody/tr[1]/td[3]/img/@ng-alt")
    private String score;

    @ExtractBy(BASE_PATH + "/table/tbody/tr[4]/td[1]/p/text()")
    private String regStatus;//状态

    @ExtractBy(BASE_PATH + "/table/tbody/tr[4]/td[2]/p/text()")
    private String regDate;//注册时间

    @ExtractBy(BASE_PATH + "/table[2]/tbody/tr[1]/td[1]/div/span/text()")
    private String industry;

    @ExtractBy(BASE_PATH + "/table[2]/tbody/tr[1]/td[2]/div/span/text()")
    private String regNum;//工商注册号

    @ExtractBy(BASE_PATH + "/table[2]/tbody/tr[2]/td[1]/div/span/text()")
    private String type;//企业类型

    @ExtractBy(BASE_PATH + "/table[2]/tbody/tr[2]/td[2]/div/span/text()")
    private String orgCode;//组织机构代码

    @ExtractBy(BASE_PATH + "/table[2]/tbody/tr[3]/td[1]/div/span/text()")
    private String businessTerm;//营业期限

    @ExtractBy(BASE_PATH + "/table[2]/tbody/tr[3]/td[2]/div/span/text()")
    private String regAuth;//登记机关

    @ExtractBy(BASE_PATH + "/table[2]/tbody/tr[4]/td[1]/div/span/text()")
    private String approvalDate;//核准日期

    @ExtractBy(BASE_PATH + "/table[2]/tbody/tr[4]/td[2]/div/span/text()")
    private String creditCode;//统一信用代码

    @ExtractBy(BASE_PATH + "/table[2]/tbody/tr[5]/td/div/span/text()")
    private String regAddress;//注册地址

    @ExtractBy(BASE_PATH + "/table[2]/tbody/tr[6]/td/div/span/span[2]/text()")
    private String businessScope;//经营范围

    private String url;//公司详情URL

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getRegFund() {
        return regFund;
    }

    public void setRegFund(String regFund) {
        this.regFund = regFund;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getBusinessTerm() {
        return businessTerm;
    }

    public void setBusinessTerm(String businessTerm) {
        this.businessTerm = businessTerm;
    }

    public String getRegAuth() {
        return regAuth;
    }

    public void setRegAuth(String regAuth) {
        this.regAuth = regAuth;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "TianYanChaInfo{" +
                "name='" + name + '\'' +
                ", legalPerson='" + legalPerson + '\'' +
                ", regFund='" + regFund + '\'' +
                ", score='" + score + '\'' +
                ", regStatus='" + regStatus + '\'' +
                ", regDate='" + regDate + '\'' +
                ", industry='" + industry + '\'' +
                ", regNum='" + regNum + '\'' +
                ", type='" + type + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", businessTerm='" + businessTerm + '\'' +
                ", regAuth='" + regAuth + '\'' +
                ", approvalDate='" + approvalDate + '\'' +
                ", creditCode='" + creditCode + '\'' +
                ", regAddress='" + regAddress + '\'' +
                ", businessScope='" + businessScope + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
