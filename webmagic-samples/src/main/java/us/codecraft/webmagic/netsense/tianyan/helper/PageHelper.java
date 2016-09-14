package us.codecraft.webmagic.netsense.tianyan.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyInfo;

public class PageHelper {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public CompanyInfo parse(Page page){
        CompanyInfo info = new CompanyInfo();

        String name = page.getHtml().xpath("//div[@class='company_info_text']/p/text()").get();

        String legalPerson = getByXpath(page, "/table/tbody/tr[2]/td[1]//a/text()");
        String regFund = getByXpath(page, "/table/tbody/tr[2]/td[2]/p/text()");
        String score = getByXpath(page, "/table[1]/tbody/tr[1]/td[3]/img/@ng-alt");
        String regStatus = getByXpath(page, "/table/tbody/tr[4]/td[1]/p/text()");
        String regDate = getByXpath(page, "/table/tbody/tr[4]/td[2]/p/text()");
        String industry = getByXpath(page, "/table[2]/tbody/tr[1]/td[1]/div/span/text()");
        String regNum = getByXpath(page, "/table[2]/tbody/tr[1]/td[2]/div/span/text()");
        String type = getByXpath(page, "/table[2]/tbody/tr[2]/td[1]/div/span/text()");
        String orgCode = getByXpath(page, "/table[2]/tbody/tr[2]/td[2]/div/span/text()");
        String businessTerm = getByXpath(page, "/table[2]/tbody/tr[3]/td[1]/div/span/text()");
        String regAuth = getByXpath(page, "/table[2]/tbody/tr[3]/td[2]/div/span/text()");
        String approvalDate = getByXpath(page, "/table[2]/tbody/tr[4]/td[1]/div/span/text()");
        String creditCode = getByXpath(page, "/table[2]/tbody/tr[4]/td[2]/div/span/text()");
        String regAddress = getByXpath(page, "/table[2]/tbody/tr[5]/td/div/span/text()");
        String businessScope = getByXpath(page, "/table[2]/tbody/tr[6]/td/div/span/text()");
        String url = page.getUrl().get();

        info.setName(name);
        info.setUrl(url);
        info.setApprovalDate(approvalDate);
        info.setBusinessScope(businessScope);
        info.setBusinessTerm(businessTerm);
        info.setCreditCode(creditCode);
        info.setIndustry(industry);
        info.setLegalPerson(legalPerson);
        info.setRegAddress(regAddress);
        info.setScore(score);
        info.setRegStatus(regStatus);
        info.setRegDate(regDate);
        info.setRegFund(regFund);
        info.setRegNum(regNum);
        info.setType(type);
        info.setOrgCode(orgCode);
        info.setRegAuth(regAuth);

        return info;
    }

    private String getByXpath(Page page, String xpath) {
        String baseXpath = "//div[@class='row b-c-white company-content']";
        String value = page.getHtml().xpath(baseXpath + xpath).get();
        logger.info("getByXpath:" + value);

        return value;
    }
}
