package us.codecraft.webmagic.netsense.stats.gov.processor;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.netsense.base.util.UrlUtils;
import us.codecraft.webmagic.netsense.downloader.JsoupDownloader;
import us.codecraft.webmagic.netsense.stats.gov.pipeline.StatsGovPipeline;
import us.codecraft.webmagic.netsense.stats.gov.pojo.Navigation;
import us.codecraft.webmagic.netsense.stats.gov.pojo.OtherWds;
import us.codecraft.webmagic.netsense.stats.gov.pojo.QueryData;
import us.codecraft.webmagic.netsense.stats.gov.pojo.Wds;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StatsGovProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static int mCount = 0;
    private static final String PRE = "http://data.stats.gov.cn/easyquery.htm?cn=";
    private static final Map<String, String> navigationMap = new HashMap<String, String>() {
        {
            put(PRE + "A01", "hgyd");//月度数据
            put(PRE + "B01", "hgjd");//季度数据
            put(PRE + "C01", "hgnd");//年度数据
            put(PRE + "E0101", "fsyd");//分省月度
            put(PRE + "E0102", "fsjd");//分省季度
            put(PRE + "E0103", "fsnd");//分省年度
            put(PRE + "E0104", "csyd");//城市月度
            put(PRE + "E0105", "csnd");//城市年度
            //少加一个就少爬一个导航类型，根据业务需求，也可以再继续多加
        }
    };

    private static Map<String, List<OtherWds>> otherWdsMap = new HashMap<String, List<OtherWds>>();

    @Override
    public void process(Page page) {

        logger.info("process(Page page)::" + page.getUrl());

        getNavigation(page);
        getTree(page);
        getQueryData(page);

        logger.info("#######################################");
    }

    private void getQueryData(Page page) {
        if (page.getUrl().get().contains("QueryData")) {
            logger.info("getQueryData Size = " + (mCount++) + " Url = " + page.getUrl().get());
            String returndata = JSON.parseObject(getJson(page)).getJSONObject("returndata").toString();
            QueryData queryData = new Gson().fromJson(returndata, QueryData.class);
            queryData.setUrl(page.getUrl().get());
            page.putField(StatsGovPipeline.PARAM_RESULT, queryData);
        }
    }

    private void getOtherWds(Page page) {
        if (page.getUrl().get().contains("getOtherWds")) {
            String dbcode = UrlUtils.getValue(page.getUrl().get(), "dbcode");
            String rowcode = UrlUtils.getValue(page.getUrl().get(), "rowcode");
            String wds = UrlUtils.getValue(page.getUrl().get(), "wds");

            List<Wds> list = new Gson().fromJson(wds, new TypeToken<List<Wds>>() {
            }.getType());
            String valuecode = list.get(0).getValuecode();
            String json = getJson(page);
            String otherWdsJsonStr = JSON.parseObject(json).getJSONArray("returndata").toString();
            List<OtherWds> otherWdsList = new Gson().fromJson(otherWdsJsonStr, new TypeToken<List<OtherWds>>() {
            }.getType());
            otherWdsMap.put(getMapKey(dbcode, rowcode, valuecode), otherWdsList);
        }

    }

    private String getMapKey(String dbcode, String rowcode, String valuecode) {
        return dbcode + "--" + rowcode + "--" + valuecode;
    }


    private void getTree(Page page) {
        if (page.getUrl().get().contains("getTree")) {
            String jsonStr = getJson(page);
            List<Navigation> navigationList = new Gson().fromJson(jsonStr, new TypeToken<List<Navigation>>() {
            }.getType());
            logger.info(navigationList.toString());
            page.putField(StatsGovPipeline.PARAM_NAVIGATION, navigationList);

            for (Navigation navigation : navigationList) {
                if (navigation.isIsParent()) {
                    page.addTargetRequest(getTreeUrl(navigation.getId(), navigation.getDbcode(), navigation.getWdcode()));
                } else {
                    String mapKey = getMapKey(navigation.getDbcode(), navigation.getWdcode(), navigation.getId());
                    if (!otherWdsMap.containsKey(mapKey)) {
                        String otherWdsUrl = getOtherWdsUrl(navigation.getDbcode(), navigation.getWdcode(), navigation.getId());
                        Page otherWdsPage = new JsoupDownloader().download(otherWdsUrl);
                        getOtherWds(otherWdsPage);
                    }

                    List<OtherWds> otherWdsList = otherWdsMap.get(mapKey);

                    //----寻找时间维度
                    OtherWds sjOtherWds = null;
                    for (OtherWds otherWds : otherWdsList) {
                        if (otherWds.issj()) {
                            sjOtherWds = otherWds;
                            break;
                        }
                    }
                    //----寻找指标维度
                    OtherWds zbOtherWds = null;
                    for (OtherWds otherWds : otherWdsList) {
                        if (!otherWds.issj()) {
                            zbOtherWds = otherWds;
                            break;
                        }
                    }
                    //拼凑dfwds=[{"wdcode":"zb","valuecode":"A01010103"},{"wdcode":"sj","valuecode":"LAST24"}]
                    List<Wds> wdsList = new ArrayList<Wds>();

                    wdsList.add(new Wds(sjOtherWds.getWdcode(), sjOtherWds.getNodes().get(sjOtherWds.getNodes().size() - 1).getCode()));

                    List<String> queryDataUrlList = new ArrayList<String>();
                    if (zbOtherWds != null) {
                        for (OtherWds.Nodes nodes : zbOtherWds.getNodes()) {
                            List<Wds> tmpList = new ArrayList<Wds>(wdsList);
                            tmpList.add(new Wds(zbOtherWds.getWdcode(), nodes.getCode()));
                            tmpList.add(new Wds(navigation.getWdcode(), navigation.getId()));
                            String dfwds = new Gson().toJson(tmpList);
                            queryDataUrlList.add(getQueryDataUrl(navigation.getDbcode(), navigation.getWdcode(), dfwds));
                        }
                    } else {
                        wdsList.add(new Wds(navigation.getWdcode(), navigation.getId()));
                        String dfwds = new Gson().toJson(wdsList);
                        queryDataUrlList.add(getQueryDataUrl(navigation.getDbcode(), navigation.getWdcode(), dfwds));
                    }
                    page.addTargetRequests(queryDataUrlList, 1);

                }
            }
        }

    }

    private void getNavigation(Page page) {
        if ("http://data.stats.gov.cn/".equals(page.getUrl().get())) {
            String content = page.getHtml().get();
            Pattern compile = Pattern.compile("href=\"(http://data\\.stats\\.gov\\.cn/easyquery.htm\\?cn=.*)\"");
            Matcher matcher = compile.matcher(content);
            while (matcher.find()) {
                String url = matcher.group(1);
                logger.info(url);
                if (navigationMap.containsKey(url)) {
                    page.addTargetRequest(getTreeUrl(navigationMap.get(url)));
                }
            }
        }
    }

    private String getQueryDataUrl(String dbcode, String wdcode, String dfwds) {
        //http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgyd&rowcode=zb&colcode=sj&wds=[]&dfwds=[{"wdcode":"zb","valuecode":"A010102"}]
        String url = "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=" + dbcode + "&rowcode=" + wdcode + "&colcode=sj&wds=[]&dfwds=" + dfwds;
        logger.info("QueryData Url = " + url);
        return url;
    }

    private String getOtherWdsUrl(String dbcode, String wdcode, String valuecode) {
        //http://data.stats.gov.cn/easyquery.htm?m=getOtherWds&dbcode=hgyd&rowcode=zb&colcode=sj&wds=[{"wdcode":"zb","valuecode":"A010101"}]
        List<Wds> list = new ArrayList<Wds>();
        Wds wds = new Wds(wdcode, valuecode);
        list.add(wds);
        String url = "http://data.stats.gov.cn/easyquery.htm?m=getOtherWds&dbcode=" + dbcode + "&rowcode=" + wdcode + "&colcode=sj&wds=" + new Gson().toJson(list);
        logger.info("getOtherWds Url = " + url);
        return url;
    }


    private String getTreeUrl(String id, String dbcode, String wdcode) {
        //拼凑左侧导航列表
        //http://data.stats.gov.cn/easyquery.htm?id=zb&dbcode=hgyd&wdcode=zb&m=getTree
        String url = "http://data.stats.gov.cn/easyquery.htm?m=getTree&id=" + id + "&dbcode=" + dbcode + "&wdcode=" + wdcode;
        logger.info("getTree Url : " + url);
        return url;
    }

    private String getTreeUrl(String dbcode) {
        String url = "http://data.stats.gov.cn/easyquery.htm?id=zb&wdcode=zb&m=getTree&dbcode=" + dbcode;
        logger.info("getTree Url : " + url);
        return url;
    }

    private String getJson(Page page) {
        return page.getHtml().xpath("//body/text()").get();
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3);
    }

    public static void main(String[] args) {
        Spider.create(new StatsGovProcessor())
                .addPipeline(new StatsGovPipeline())
                .setDownloader(new JsoupDownloader())
                .scheduler(new PriorityScheduler())
                .addUrl("http://data.stats.gov.cn/")
                .thread(8)
                .start();
    }
}
