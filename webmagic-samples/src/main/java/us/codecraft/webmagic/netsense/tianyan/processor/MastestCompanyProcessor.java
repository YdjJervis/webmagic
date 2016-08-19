package us.codecraft.webmagic.netsense.tianyan.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.netsense.tianyan.model.CompanyHouseModel;
import us.codecraft.webmagic.netsense.tianyan.model.CompanyModel;
import us.codecraft.webmagic.netsense.tianyan.pipeline.DetailsPipeline;
import us.codecraft.webmagic.netsense.tianyan.pojo.Company;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyInfo;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyResult;
import us.codecraft.webmagic.netsense.tianyan.pojo.RelationShip;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全国500强企业
 */
public class MastestCompanyProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3);
    public static final String DETAILS = "result_param_1";
    public static final String LIST = "result_param_2";

    private static final String TAG = "DetailsProcessor::";

    @Override
    public void process(Page page) {
        System.out.println(TAG + "搜索的URL：" + page.getUrl());

        String reg = "href=\"(http://www.tianyancha.com/company/.*?)\"";

        if (page.getUrl().toString().contains("search")) {
            System.out.println(TAG + "此为搜索列表页面");

            List<Selectable> searchResultNodeList = page.getHtml().xpath("//div[@class='b-c-white search_result_container']/div").nodes();


            List<CompanyResult> companyResultList = new ArrayList<CompanyResult>();
            for (int i = 0; i < searchResultNodeList.size(); i++) {
                Selectable selectable = searchResultNodeList.get(i);
                CompanyResult cr = new CompanyResult();
                String name = selectable.xpath("//span[@ng-bind-html='node.name | trustHtml']").get();

                if (StringUtils.isEmpty(name)) {
                    continue;
                }

                Matcher matcher = Pattern.compile("([\\u4E00-\\u9FFF(（）)]+)").matcher(name);
                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                    sb.append(matcher.group(1));
                }
                name = sb.toString();

                if (StringUtils.isEmpty(name)) {
                    continue;
                }

                System.out.println(TAG + "企业名称：" + name);
                cr.setName(name);
                cr.setUrl(selectable.xpath("//a[@class='query_name']/@href").get());
                System.out.println(TAG + "列表结果：" + i + " - " + cr);
                companyResultList.add(cr);
            }

            if (CollectionUtils.isEmpty(companyResultList)) {
                crawlNext();
            }

            for (int i = 0; i < companyResultList.size(); i++) {
                if (i == 1) break;
                CompanyResult result = companyResultList.get(i);
                if (mCompanyModel.isExsit(result.getName())) {
                    crawlNext();
                } else {
                    page.addTargetRequest(result.getUrl());
                }
            }

        } else if (page.getUrl().toString().contains("company")) {
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

            if (StringUtils.isNotEmpty(name)) {
                page.putField(DETAILS, info);

                System.out.println(TAG + "对外投资::==========");
                List<Selectable> nodes = page.getHtml().xpath("//div[@ng-if='company.investList.length>0']/div/div").nodes();

                if (CollectionUtils.isNotEmpty(nodes)) {
                    List<RelationShip> rsList = new ArrayList<RelationShip>();
                    for (Selectable node : nodes) {
                        String investCompany = node.xpath("//a/text()").get();
                        String icUrl = node.xpath("//a/@href").get();
                        String money = node.xpath("//p[@class='ng-binding']/text()").get();
                        System.out.println(TAG + "对外投资公司：" + investCompany + " 链接：" + icUrl + " 投资金额：" + money);

                        RelationShip rs = new RelationShip();
                        rs.setSrcCompany(name);
                        rs.setDesCompany(investCompany);
                        rs.setDesUrl(icUrl);
                        rs.setFound(money);
                        rs.setUrl(page.getUrl().get());

                        rsList.add(rs);
                    }
                    page.putField(LIST, rsList);
                }
            } else {
                System.out.println(TAG + "没有成功解析详情页");
            }
            crawlNext();

        } else {
            System.out.println(TAG + "没有搜索到公司列表");
            crawlNext();
        }

    }

    private void crawlNext() {
        String next = next();
        if (null != next) {
            mSpider.addUrl(next);
        }
    }

    private String getByXpath(Page page, String xpath) {
        String baseXpath = "//div[@class='row b-c-white company-content']";
        String value = page.getHtml().xpath(baseXpath + xpath).get();
        System.out.println(TAG + "getByXpath:" + value);

        return value;
    }

    @Override
    public Site getSite() {
        return site;
    }

    private static Spider mSpider = Spider
            .create(new MastestCompanyProcessor())
            .addPipeline(new DetailsPipeline())
            .thread(1);
    private static int mIndex = 0;
    private static String[] mUrls;
    private static CompanyHouseModel mCompanyHouseModel;
    private static CompanyModel mCompanyModel;

    public static void main(String[] args) {
        SeleniumDownloader mDownloader = new SeleniumDownloader("E:\\softsare\\chromedriver.exe").setSleepTime(10 * 1000);
        mSpider.setDownloader(mDownloader);
        mCompanyHouseModel = new CompanyHouseModel();
        mCompanyModel = new CompanyModel();

        List<Company> companyList = mCompanyHouseModel.getListFromFile();

        List<String> srcList = new ArrayList<String>();
        for (Company company : companyList) {
            srcList.add(company.getName());
        }

        srcList.removeAll(mCompanyModel.getNameList());

        List<String> urlList = new ArrayList<String>();
        for (String companyName : srcList) {
            String url = "http://www.tianyancha.com/search/" + companyName + "?type=company";
            urlList.add(url);
        }
        System.out.println(TAG + "表格中剩余的公司数量：" + urlList.size());
        String[] urls = new String[urlList.size()];

        mUrls = urlList.toArray(urls);

        String next = next();
        if (StringUtils.isNotEmpty(next)) {
            mSpider.addUrl(next).start();
        }

    }

    private static String next() {
        if (mIndex < mUrls.length) {
            return mUrls[mIndex++];
        }
        System.out.println(TAG + "剩余数据数量：" + (mUrls.length - mIndex));
        return null;
    }
}
