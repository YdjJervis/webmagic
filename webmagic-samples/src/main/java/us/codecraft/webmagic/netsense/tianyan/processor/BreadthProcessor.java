package us.codecraft.webmagic.netsense.tianyan.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.FireFoxDownloader;
import us.codecraft.webmagic.netsense.base.util.UserAgentUtil;
import us.codecraft.webmagic.netsense.tianyan.helper.PageHelper;
import us.codecraft.webmagic.netsense.tianyan.pipeline.DetailsPipeline;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyFailure;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyInfo;
import us.codecraft.webmagic.netsense.tianyan.pojo.RelationShip;
import us.codecraft.webmagic.netsense.tianyan.pojo.SearchParamMap;
import us.codecraft.webmagic.netsense.tianyan.util.ThreadUtil;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.PriorityScheduler;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * 爬取房地产业下，每个城市前800强的公司信息
 */
public class BreadthProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(1).setUserAgent(UserAgentUtil.getRandomUserAgent()).setHttpProxy(new HttpHost("172.16.7.1445", 80));

    public static final String DETAILS = "result_param_1";
    public static final String LIST = "result_param_2";
    public static final String FAILURE = "result_param_3";
    private static final String LAYER = "layer";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void process(Page page) {
        Object larerObj = page.getRequest().getExtra(LAYER);
        logger.info("搜索的URL：" + page.getUrl() + " Layer = " + larerObj + "queue left = " + page.getTargetRequests().size());
        site.setUserAgent(UserAgentUtil.getRandomUserAgent());

        if (page.getUrl().toString().contains("company")) {
            CompanyInfo info = new PageHelper().parse(page);

            if (StringUtils.isNotEmpty(info.getName())) {
                page.putField(DETAILS, info);

                logger.info("对外投资::==========");
                List<Selectable> nodes = page.getHtml().xpath("//div[@ng-if='company.investList.length>0']/div/div").nodes();

                if (CollectionUtils.isNotEmpty(nodes)) {
                    List<RelationShip> rsList = new ArrayList<RelationShip>();
                    for (Selectable node : nodes) {

                        RelationShip rs = new RelationShip();

                        String investCompany = node.xpath("//a/text()").get();
                        investCompany  = node.xpath("//a/span/text()").get();

                        String icUrl = node.xpath("//a/@href").get();
                        icUrl = node.xpath("//a/@href").get();

                        String money = node.xpath("//p[@class='ng-binding']/text()").get();
                        money = node.xpath("//p/span[2]/text()").get();

                        logger.info("对外投资公司：" + investCompany + " 链接：" + icUrl + " 投资金额：" + money);

                        rs.setSrcCompany(info.getName());
                        rs.setDesCompany(investCompany);
                        rs.setDesUrl(icUrl);
                        rs.setFound(money);
                        rs.setUrl(page.getUrl().get());

                        Request request = new Request(rs.getDesUrl());
                        request.putExtra(LAYER, larerObj == null ? 1 : (Integer) larerObj + 1);

                        page.addTargetRequest(request);

                        rsList.add(rs);
                    }
                    page.putField(LIST, rsList);
                }
            } else {
                logger.info("没有成功解析详情页,一般是被限制了");
                //解析失败，等待一个小时，服务器拨号后继续抓取
                page.putField(FAILURE, generateFailure(page));
                ThreadUtil.sleep(30);
            }

        } else {
            page.putField(FAILURE, generateFailure(page));
            ThreadUtil.sleep(30);
        }

    }

    private CompanyFailure generateFailure(Page page) {
        CompanyFailure failure = new CompanyFailure();
        failure.setUrl(page.getUrl().get());
        failure.setLayer(String.valueOf(page.getRequest().getExtra(LAYER)));
        return failure;
    }

    @Override
    public Site getSite() {
        return site;
    }

    private static Spider mSpider = Spider
            .create(new BreadthProcessor())
            .addPipeline(new DetailsPipeline())
            .thread(1);

    public static void main(String[] args) {
        FireFoxDownloader downloader = new FireFoxDownloader("E:\\softsare\\web245\\hhllq_Firefox_gr\\App\\Firefox\\firefox.exe")
//        FireFoxDownloader downloader = new FireFoxDownloader("D:\\web245\\hhllq_Firefox_gr\\App\\Firefox\\firefox.exe")
                .setSleepTime(20 * 1000)
                .setProxy("172.16.7.144", 9090);

        mSpider.setDownloader(downloader).setScheduler(new PriorityScheduler());
        String[] urls = new SearchParamMap().getTop100Urls();
        mSpider.addUrl(urls);
        mSpider.start();
    }

}
