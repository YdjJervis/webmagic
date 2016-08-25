package us.codecraft.webmagic.netsense.tianyan.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.FireFoxDownloader;
import us.codecraft.webmagic.netsense.Context;
import us.codecraft.webmagic.netsense.base.util.UserAgentUtil;
import us.codecraft.webmagic.netsense.tianyan.dao.CompanyDao;
import us.codecraft.webmagic.netsense.tianyan.pipeline.DetailsPipeline;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyInfo;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyResult;
import us.codecraft.webmagic.netsense.tianyan.pojo.RelationShip;
import us.codecraft.webmagic.netsense.tianyan.pojo.SearchParamMap;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.PriorityScheduler;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬取房地产业下，每个城市前800强的公司信息
 */
public class FirstSendCityProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(1).setUserAgent(UserAgentUtil.getRandomUserAgent()).setHttpProxy(new HttpHost("172.16.7.1445", 80));

    public static final String DETAILS = "result_param_1";
    public static final String LIST = "result_param_2";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private CompanyDao mCompanyDao = (CompanyDao) Context.getInstance().getBean("companyDao");

    @Override
    public void process(Page page) {
        logger.info("搜索的URL：" + page.getUrl());
        site.setUserAgent(UserAgentUtil.getRandomUserAgent());

        if (page.getUrl().toString().contains("search")) {
            logger.info("此为搜索列表页面");

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

                cr.setName(name);
                cr.setUrl(selectable.xpath("//a[@class='query_name']/@href").get());
                logger.info("列表结果：" + i + " - " + cr);
                companyResultList.add(cr);
            }

            List<String> needToCrawlList = new ArrayList<String>();
            for (CompanyResult result : companyResultList) {
                if (!mCompanyDao.isExist(result.getUrl())) {
                    logger.info("加入下载队列(详情)：" + result.getUrl());
                    needToCrawlList.add(result.getUrl());
                }
            }
            page.addTargetRequests(needToCrawlList, 1);

            List<String> all = page.getHtml().xpath("//li[@class='pagination-page ng-scope']/a/@href").all();
            if(CollectionUtils.isNotEmpty(all)){
                String pageUrl = all.get(0);
                List<String> pageNumList = page.getHtml().xpath("//li[@class='pagination-page ng-scope']/a/text()").all();
                for (String pageNum : pageNumList) {
                    logger.info("加入下载队列(翻页)：" + getPagedUrl(pageUrl, pageNum));
                    page.addTargetRequest(getPagedUrl(pageUrl, pageNum));
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

                logger.info("对外投资::==========");
                List<Selectable> nodes = page.getHtml().xpath("//div[@ng-if='company.investList.length>0']/div/div").nodes();

                if (CollectionUtils.isNotEmpty(nodes)) {
                    List<RelationShip> rsList = new ArrayList<RelationShip>();
                    for (Selectable node : nodes) {
                        String investCompany = node.xpath("//a/text()").get();
                        String icUrl = node.xpath("//a/@href").get();
                        String money = node.xpath("//p[@class='ng-binding']/text()").get();
                        logger.info("对外投资公司：" + investCompany + " 链接：" + icUrl + " 投资金额：" + money);

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
                logger.info("没有成功解析详情页,一般是被限制了");
                //解析失败，等待一个小时，服务器拨号后继续抓取
//                sleep(60);
            }

        } else {
//            sleep(60);
        }

    }

    /**
     * @param timeMin 分钟数
     */
    private void sleep(long timeMin) {
        try {
            Thread.sleep(timeMin * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据页码生产新的URL
     */
    private String getPagedUrl(String url, String page) {
        if (NumberUtils.isNumber(page)) {
            if (Integer.valueOf(page) > 10) {//一个城市只爬取10个页面，合200个公司，一，二线总共13个城市
                return url;
            }
            String[] split = url.split("\\?");
            return "http://www.tianyancha.com/search/page/" + page + "?" + split[1];
        } else {
            return url;
        }
    }

    private String getByXpath(Page page, String xpath) {
        String baseXpath = "//div[@class='row b-c-white company-content']";
        String value = page.getHtml().xpath(baseXpath + xpath).get();
        logger.info("getByXpath:" + value);

        return value;
    }

    @Override
    public Site getSite() {
        return site;
    }

    private static Spider mSpider = Spider
            .create(new FirstSendCityProcessor())
            .addPipeline(new DetailsPipeline())
            .thread(1);

    public static void main(String[] args) {
//        FireFoxDownloader downloader = new FireFoxDownloader("E:\\softsare\\web245\\hhllq_Firefox_gr\\App\\Firefox\\firefox.exe")
        FireFoxDownloader downloader = new FireFoxDownloader("D:\\web245\\hhllq_Firefox_gr\\App\\Firefox\\firefox.exe")
                .setSleepTime(15 * 1000)
                .setProxy("172.16.7.144", 9090);

        mSpider.setDownloader(downloader).setScheduler(new PriorityScheduler());
        String[] urls = new SearchParamMap().getFirstSeondCityUrls();
        mSpider.addUrl(urls);
        mSpider.start();
    }

}
