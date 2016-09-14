package us.codecraft.webmagic.netsense.qichacha.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.netsense.base.util.UrlUtils;
import us.codecraft.webmagic.netsense.base.util.UserAgentUtil;
import us.codecraft.webmagic.netsense.qichacha.model.CompanyModel;
import us.codecraft.webmagic.netsense.qichacha.pipeline.CompanyPipeline;
import us.codecraft.webmagic.netsense.qichacha.pojo.SearchResult;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyInfo;
import us.codecraft.webmagic.netsense.tianyan.pojo.RelationShip;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * 企查查
 */
public class CompanyProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(1).setUserAgent(UserAgentUtil.getRandomUserAgent()).setSleepTime(1000).setTimeOut(1000);
    private static final String SEARCH_RESULT = "search_result";

    @Override
    public void process(Page page) {
        dealSearchResult(page);
        dealBaseInfo(page);
        dealTouziInfo(page);
    }

    private void dealSearchResult(Page page) {
        if (page.getUrl().get().contains("search")) {
            List<Selectable> resultNodes = page.getHtml().xpath("//tr[@class='table-search-list']").nodes();

            List<SearchResult> searchResultList = new ArrayList<SearchResult>();
            for (Selectable resultNode : resultNodes) {
                String name = resultNode.xpath("//a[@class='text-priamry']/html()").get().replaceAll("<em>", "").replaceAll("</em>", "");
                String url = resultNode.xpath("//a[@class='text-priamry']").regex("href=\"(http://www.qichacha.com/firm_.*.shtml)\"").get();

                SearchResult searchResult = new SearchResult(name, url);
                System.out.println(searchResult);

                searchResultList.add(searchResult);
            }

            for (SearchResult result : searchResultList) {
                if (result.getName().equals(UrlUtils.getValue(page.getUrl().get(), "key"))) {
                    requestAssemblyPage(page, result);
                    break;
                }
            }
        }
    }

    private void requestAssemblyPage(Page page, SearchResult result) {
        String baseInfoUrl = "http://www.qichacha.com/company_getinfos?tab=base&unique=" + result.getUniqueCode() + "&companyname=" + result.getName();
        String touziUrl = "http://www.qichacha.com/company_getinfos?tab=touzi&unique=" + result.getUniqueCode() + "&companyname=" + result.getName();

        Request request = new Request(baseInfoUrl);
        request.putExtra(SEARCH_RESULT, result);
        page.addTargetRequest(request);

        request = new Request(touziUrl);
        request.putExtra(SEARCH_RESULT, result);
        page.addTargetRequest(request);
    }

    private void dealBaseInfo(Page page) {

        if (page.getUrl().get().contains("company_getinfos?tab=base")) {

            SearchResult result = (SearchResult) page.getRequest().getExtra(SEARCH_RESULT);
            String name = result.getName();
            String regFund = getValue(page, "注册资本");
            String legalPerson = getValue(page, "法定代表");
            String regStatus = getValue(page, "经营状态");
            String regDate = getValue(page, "成立日期");
            String industry = getValue(page, "所属行业");
            String regNum = getValue(page, "注册号");
            String type = getValue(page, "公司类型");
            String orgCode = getValue(page, "组织机构代码");
            String businessTerm = getValue(page, "营业期限");
            String regAuth = getValue(page, "登记机关");
            String approvalDate = getValue(page, "发照日期");
            String creditCode = getValue(page, "信用代码");
            String regAddress = getValue(page, "企业地址");
            String businessScope = getValue(page, "经营范围");
            String url = page.getUrl().get();

            CompanyInfo info = new CompanyInfo();
            info.setName(name);
            info.setLegalPerson(legalPerson);
            info.setRegFund(regFund);
            info.setRegStatus(regStatus);
            info.setRegDate(regDate);
            info.setRegNum(regNum);
            info.setIndustry(industry);
            info.setType(type);
            info.setOrgCode(orgCode);
            info.setBusinessTerm(businessTerm);
            info.setRegAddress(regAddress);
            info.setRegAuth(regAuth);
            info.setApprovalDate(approvalDate);
            info.setCreditCode(creditCode);
            info.setBusinessScope(businessScope);
            info.setUrl(url);

            System.out.println(info);

            page.putField(CompanyPipeline.PARAM_INFO, info);
        }
    }

    private String getValue(Page page, String key) {
        List<Selectable> nodes = page.getHtml().xpath("//ul[@class='company-base']/li").nodes();
        for (Selectable node : nodes) {
            if (node.xpath("label").get().contains(key)) {
                if (key.contains("法定代表")) {
                    return node.xpath("//a[@class='text-primary']/text()").get().trim();
                } else {
                    return node.xpath("li/text()").get().trim();
                }
            }
        }
        return "";
    }

    private void dealTouziInfo(Page page) {

        if (page.getUrl().get().contains("company_getinfos?tab=touzi")) {
            SearchResult result = (SearchResult) page.getRequest().getExtra(SEARCH_RESULT);
            List<RelationShip> relationShipList = new ArrayList<RelationShip>();

            List<Selectable> touziNodes = page.getHtml().xpath("//a[@class='list-group-item clearfix']").nodes();
            for (Selectable touziNode : touziNodes) {
                RelationShip relationShip = new RelationShip();

                String desCompany = touziNode.xpath("//span[@class='text-lg']/text()").get();
                String found = touziNode.xpath("//small[@class='text-gray clear text-ellipsis m-t-xs text-md']/text()").get();
                String url = touziNode.regex("href=\"(.*?)\"").get();

                relationShip.setSrcCompany(result.getName());
                relationShip.setDesCompany(desCompany);
                relationShip.setUrl(result.getUrl());
                relationShip.setDesUrl("http://www.qichacha.com" + url);
                relationShip.setFound(found);
                relationShip.setLayer(result.getLayer());

                relationShipList.add(relationShip);

                SearchResult searchResult = new SearchResult(relationShip.getDesCompany(), relationShip.getUrl());
                searchResult.setLayer(result.getLayer() + 1);
                requestAssemblyPage(page, searchResult);

                page.putField(CompanyPipeline.PARAM_LIST, relationShipList);
            }

            List<String> pageNoList = page.getHtml().xpath("//ul[@class='pagination pagination-md']//a/text()").regex("([0-9]+)").all();
            for (String pageNo : pageNoList) {
                String pageUrl = UrlUtils.setValue(page.getUrl().get(), "p", pageNo);
                System.out.println("翻页URL：" + pageUrl);
                Request request = new Request(pageUrl);
                request.putExtra(SEARCH_RESULT, result);

                page.addTargetRequest(request);
            }
        }
    }

    private static Spider mSpider = Spider
            .create(new CompanyProcessor())
            .addPipeline(new CompanyPipeline())
            .thread(1);

    @Override
    public Site getSite() {
        site.addHeader("Host", "www.qichacha.com");
        site.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0\\r\\n");
        site.addHeader("Accept", "text/html, */*; q=0.01\\r\\n");
        site.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3\\r\\n");
        site.addHeader("Accept-Encoding", "gzip, deflate\\r\\n");
        site.addHeader("X-Requested-With", "XMLHttpRequest\\r\\n");
        site.addHeader("Referer", "http://www.qichacha.com/firm_6bc7e7ccdb755391651316a0227c059b.shtml\\r\\n");
        site.addHeader("Cookie", "PHPSESSID=a6tgq7etihjbo0cb0o9i61ovn1; gr_user_id=9b7a81cb-3fa4-49da-aac0-dc0c8951ae3a; CNZZDATA1254842228=449736588-1473731028-%7C1473823361; _uab_collina=147373238127841915486054; gr_session_id_9c1eb7420511f8b2=987139a6-5cff-4d68-ab2d-b9e3a2dad959");
        site.addHeader("Connection", "keep-alive\\r\\n");
        return site;
    }

    public static void main(String[] args) {
        mSpider.addUrl(new CompanyModel().getUrls()).start();
//        mSpider.addUrl("http://www.qichacha.com/company_getinfos?tab=base&unique=6bc7e7ccdb755391651316a0227c059b&companyname=万科企业股份有限公司").start();
//        mSpider.addUrl("http://www.qichacha.com/company_getinfos?tab=touzi&unique=6bc7e7ccdb755391651316a0227c059b&companyname=万科企业股份有限公司").start();

    }
}
