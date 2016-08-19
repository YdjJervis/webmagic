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
import us.codecraft.webmagic.netsense.stats.gov.pipeline.MonthDataPipeline;
import us.codecraft.webmagic.netsense.stats.gov.pojo.Navigation;
import us.codecraft.webmagic.netsense.stats.gov.pojo.OtherWds;
import us.codecraft.webmagic.netsense.stats.gov.pojo.Wds;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MonthDataProcessor implements PageProcessor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String PRE = "http://data.stats.gov.cn/easyquery.htm?cn=";
    private static final Map<String, String> map = new HashMap<String, String>() {
        {
            put(PRE + "A01", "hgyd");
//            put(PRE + "B01", "hgjd");
//            put(PRE + "C01", "hgnd");
//            put(PRE + "E0101", "fsyd");
//            put(PRE + "E0102", "fsjd");
//            put(PRE + "E0103", "fsnd");
//            put(PRE + "E0104", "csyd");
//            put(PRE + "E0105", "csnd");
        }
    };

    private static Map<String, List<OtherWds>> otherWdsMap = new HashMap<String, List<OtherWds>>();

    @Override
    public void process(Page page) {

        logger.info("process..." + page.getUrl());

        getNavigation(page);
        getOtherWds(page);
        getTree(page);

        logger.info("process end");
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

            for (Navigation navigation : navigationList) {
                if (navigation.isParent()) {
                    page.addTargetRequest(getTreeUrl(navigation.getId(), navigation.getDbcode(), navigation.getWdcode()));
                } else {
                    if (!otherWdsMap.containsKey(getMapKey(navigation.getDbcode(), navigation.getWdcode(), navigation.getId()))) {
                        String otherWdsUrl = getOtherWdsUrl(navigation.getDbcode(), navigation.getWdcode(), navigation.getId());
                        page.addTargetRequest(otherWdsUrl);
                        logger.info(otherWdsUrl);
                    }

                }
            }

            logger.info(navigationList.toString());
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
                if (map.containsKey(url)) {
                    page.addTargetRequest(getTreeUrl(map.get(url)));
                }
            }
        }
    }

    private String getQueryDataUrl(String dbcode, String wdcode, String valuecode) {
        //http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=hgyd&rowcode=zb&colcode=sj&wds=[]&dfwds=[{"wdcode":"zb","valuecode":"A010102"}]
        List<Wds> list = new ArrayList<Wds>();
        Wds wds = new Wds(wdcode, valuecode);
        list.add(wds);
        return "http://data.stats.gov.cn/easyquery.htm?m=QueryData&dbcode=" + dbcode + "&rowcode=" + wdcode + "&colcode=sj&wds=[]&dfwds=" + new Gson().toJson(list);
    }

    private String getOtherWdsUrl(String dbcode, String wdcode, String valuecode) {
        //http://data.stats.gov.cn/easyquery.htm?m=getOtherWds&dbcode=hgyd&rowcode=zb&colcode=sj&wds=[{"wdcode":"zb","valuecode":"A010101"}]
        List<Wds> list = new ArrayList<Wds>();
        Wds wds = new Wds(wdcode, valuecode);
        list.add(wds);
        return "http://data.stats.gov.cn/easyquery.htm?m=getOtherWds&dbcode=" + dbcode + "&rowcode=" + wdcode + "&colcode=sj&wds=" + new Gson().toJson(list);
    }


    private String getTreeUrl(String id, String dbcode, String wdcode) {
        //拼凑左侧导航列表
        //http://data.stats.gov.cn/easyquery.htm?id=zb&dbcode=hgyd&wdcode=zb&m=getTree
        String url = "http://data.stats.gov.cn/easyquery.htm?m=getTree&id=" + id + "&dbcode=" + dbcode + "&wdcode=" + wdcode;
        logger.info("tree url : " + url);
        return url;
    }

    private String getTreeUrl(String dbcode) {
        return "http://data.stats.gov.cn/easyquery.htm?id=zb&wdcode=zb&m=getTree&dbcode=" + dbcode;
    }

    private String getJson(Page page) {
        return page.getHtml().xpath("//body/text()").get();
    }

    @Override
    public Site getSite() {
        return Site.me().setRetryTimes(3);
    }

    public static void main(String[] args) {
        Spider.create(new MonthDataProcessor())
                .addPipeline(new MonthDataPipeline())
                .setDownloader(new JsoupDownloader())
                .addUrl("http://data.stats.gov.cn/")
                .thread(3)
                .start();
    }
}
