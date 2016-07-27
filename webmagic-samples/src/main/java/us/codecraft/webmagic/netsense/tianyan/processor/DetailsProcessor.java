package us.codecraft.webmagic.netsense.tianyan.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.netsense.tianyan.model.CompanyModel;
import us.codecraft.webmagic.netsense.tianyan.pipeline.DetailsPipeline;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyCvs;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyInfo;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyResult;
import us.codecraft.webmagic.netsense.tianyan.pojo.RelationShip;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * 天眼查详情页面
 */
public class DetailsProcessor implements PageProcessor {

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

            /*List<String> urlList = page.getHtml().regex("href=\"(http://www.tianyancha.com/company*//*)\"").all();
            System.out.println(TAG + "搜索公司列表::" + urlList);

            for (String url : urlList) {
                System.out.println(TAG + "搜索列表URL:" + url);
            }*/

            List<Selectable> searchResultNodeList = page.getHtml().xpath("//div[@class='b-c-white search_result_container']/div").nodes();
            System.out.println(TAG + "000");
            List<CompanyResult> companyResultList = new ArrayList<CompanyResult>();
            for (int i = 0; i < searchResultNodeList.size(); i++) {
                Selectable selectable = searchResultNodeList.get(i);
                CompanyResult cr = new CompanyResult();
                String name = selectable.xpath("//span[@ng-bind-html='node.name | trustHtml']").get();
                if (StringUtils.isEmpty(name)) {
                    continue;
                }
                System.out.println(TAG + "企业名称：" + name);
                cr.setName(name.replaceAll("<em>", "").replaceAll("</em>", ""));
                cr.setUrl(selectable.xpath("//a[@class='query_name']/@href").get());
                System.out.println(TAG + "列表结果：" + i + " - " + cr);
                companyResultList.add(cr);
            }
            System.out.println(TAG + "111");
            if (CollectionUtils.isNotEmpty(companyResultList)) {
                System.out.println(TAG + "222");
                crawlNext();
                return;
            }
            System.out.println(TAG + "333");
            for (int i = 0; i < companyResultList.size(); i++) {
                if (i == 1) break;
                CompanyResult result = companyResultList.get(i);
                if (mCompanyModel.isExsit(result.getName())) {
                    System.out.println(TAG + "444");
                    crawlNext();
                    return;
                } else {
                    System.out.println(TAG + "555");
                    page.addTargetRequest(result.getUrl());
                }
            }
            System.out.println(TAG + "666");
            /*List<String> urlList = new ArrayList<String>();
            Pattern compile = Pattern.compile(reg);
            Matcher matcher = compile.matcher(page.getHtml().get());
            while (matcher.find()) {
                urlList.add(matcher.group(1));
            }

            System.out.println(TAG + "列表页列表：" + urlList);
            if (CollectionUtils.isEmpty(urlList)) {
                crawlNext();
            }

            for (int i = 0; i < urlList.size(); i++) {
                if (i == 1) break;
                System.out.println(TAG + "准备搜索详情:" + urlList.get(i));
                page.addTargetRequest(urlList.get(i));
            }*/
            crawlNext();
        } else if (page.getUrl().toString().contains("company")) {
            CompanyInfo info = new CompanyInfo();

            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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
            .create(new DetailsProcessor())
            .addPipeline(new DetailsPipeline())
            .thread(1);
    private static int mIndex = 0;
    private static String[] mUrls;
    private static CompanyModel mCompanyModel;

    public static void main(String[] args) {
        SeleniumDownloader mDownloader = new SeleniumDownloader("/home/hihi/opt/env/java/chromedrivers/chromedriver2.9").setSleepTime(3000);
        mSpider.setDownloader(mDownloader);

        mCompanyModel = new CompanyModel();
        List<CompanyCvs> companyCvsList = mCompanyModel.getList();

        List<String> srcList = new ArrayList<String>();
        for (CompanyCvs companyCvs : companyCvsList) {
            srcList.add(companyCvs.getName());
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

        /*Spider.create(new DetailsProcessor())
                .addUrl(urls)
                .setDownloader(new SeleniumDownloader("/home/hihi/opt/env/java/chromedrivers/chromedriver2.9"))
                .addPipeline(new DetailsPipeline())
                .thread(1)
                .run();*/

        String next = next();
        if (null != next) {
            mSpider.addUrl(next).start();
        }

//        mSpider.addUrl("http://www.tianyancha.com/search/" + "网信数据" + "?type=company").start();
    }

    private static String next() {
        if (mIndex < mUrls.length) {
            return mUrls[mIndex++];
        }
        return null;
    }
}
