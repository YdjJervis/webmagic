package us.codecraft.webmagic.samples.amazon.processor;

import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientImplDownloader;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.GoodsRankInfo;
import us.codecraft.webmagic.samples.amazon.pojo.RankSearchKeyword;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;
import us.codecraft.webmagic.samples.base.service.UserAgentService;
import us.codecraft.webmagic.selector.Selectable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hardy
 * @version V0.1
 *          搜索品类下关键词的排名processor，并获取前十名的商品信息
 *          2016/11/30 15:07
 */
@Service
public class SearchRankMonitorProcessor extends BasePageProcessor implements ScheduledTask {

    Logger sLogger = Logger.getLogger(getClass());
    private Set<Integer> mDealSet = Sets.newHashSet(0, 200, 402, 403, 404, 407, 417, 429, 503);
    private static final String US = "https://www.amazon.com";
    private us.codecraft.webmagic.Site mSite = us.codecraft.webmagic.Site.me().setRetryTimes(3).setSleepTime(10 * 1000).setTimeOut(10 * 1000);

    @Autowired
    UserAgentService mUserAgentService;

    @Autowired
    private HttpClientImplDownloader mHttpClientImplDownloader;

    @Override
    protected void dealReview(Page page) {
        boolean isFind = false;
        Url url = (Url) page.getRequest().getExtra(URL_EXTRA);
        /*获取当前页的商品结点列表*/
        List<Selectable> goodsNodesList = extractGoodsNodesList(page);

        /*判断是否是首页*/
        if (isHomePage(url.url)) {
            getRankTop10GoodsInfo(goodsNodesList);
            Map<String, Integer> homePage = getHomePageInfo(page, goodsNodesList);
            /*将最大页数与每页的商品数更新到监测表中*/
        }

        int rankNum = 0;
        /*查询客户商品在对应的关键词下的搜索排名*/
        if (goodsNodesList != null && goodsNodesList.size() > 0) {
            for (Selectable goodsNode : goodsNodesList) {
                /*解析商品ASIN*/
                String asin = getGoodsAsin(goodsNode);
                /*获取排名*/
                rankNum = getRankInfo(goodsNode);
                /*搜索到300名还没找到对应的客户商品，将停止搜索*/
                if (rankNum > 300) {
                    sLogger.info("搜索前300名，没有找到对应的商品.");
                    isFind = true;
                    break;
                }
                /*当前商品与客户监测的商品ASIN一致则找到其排名，停止搜索*/
                if (asin.equalsIgnoreCase(url.asin)) {
                    isFind = true;
                    break;
                }
            }
        }
        if (isFind) {
            /*更新搜索关键词排名状态*/
            System.out.println(rankNum);
        } else {
            /*如果是最后一页，则更新监测状态完成，否则将下一页的URL添加到URL表中*/
            if (isLastPage(page)) {
                /*更新搜索关键词排名状态*/
                System.out.println(rankNum);
            } else {
                sLogger.info("url(" + url.url + ")没有搜到商品.");
                /*将下一页的URL放入到URL表中*/
            }
        }
    }

    /**
     * 解析排名前十商品信息，并入库
     */
    void getRankTop10GoodsInfo(List<Selectable> goodsNodesList) {
        int rankIndex = 0;
            /*解析前十的商品信息*/
        for (Selectable goodsNode : goodsNodesList) {
            rankIndex++;
            GoodsRankInfo goodsRankInfo = extractGoodsNode(goodsNode);
            if (rankIndex == 10) {
                break;
            }
                /*将排名前十的商品信息入库*/
            System.out.println(goodsRankInfo);
        }
    }

    /**
     * 解析商品排行信息列表
     */
    List<Selectable> extractGoodsNodesList(Page page) {
        return page.getHtml().xpath("//*[@id=s-results-list-atf]/li").nodes();
    }

    /**
     * 判断商品是不是广告
     */
    boolean isAdvert(Selectable goodsNode) {
        String ad = goodsNode.xpath("//h5[contains(@class, 'contains')]").get();
        return StringUtils.isNotEmpty(ad);
    }

    /**
     * 判断是否是第一页
     */
    boolean isHomePage(String url) {
        return url.contains("page");
    }

    /**
     * 解析关键词搜索得到的商品信息
     */
    GoodsRankInfo extractGoodsNode(Selectable goodsNode) {
        /*获取商品asin*/
        GoodsRankInfo goodsRankInfo = new GoodsRankInfo();
        String title = goodsNode.xpath("//h2[contains(@class, 's-access-title')]/text()").get();
        String price = getPrice(goodsNode);
        String pictureUrl = goodsNode.xpath("//img[contains(@class, 'cfMarker')]/@src").get();
        int offersNum = getOffersNum(goodsNode);
        String departmentInfo = getDepartmentInfo(goodsNode);
        String deliveryMode = getDeliveryMode(goodsNode);
        String goodsStatus = getGoodsStatus(goodsNode);
        String distributionMode = getDistributionMode(goodsNode) ? "free" : "notFree";
        goodsRankInfo.setTitle(title);
        goodsRankInfo.setPrice(price);
        goodsRankInfo.setGoodsPictureUrl(pictureUrl);
        goodsRankInfo.setOffersNum(offersNum);
        goodsRankInfo.setDepartmentInfo(departmentInfo);
        goodsRankInfo.setDeliveryMode(deliveryMode);
        goodsRankInfo.setGoodsStatus(goodsStatus);
        goodsRankInfo.setDistributionMode(distributionMode);
        return goodsRankInfo;
    }

    /**
     * 解析商品ASIN
     */
    String getGoodsAsin(Selectable goodsNode) {
        return goodsNode.xpath("/li/@data-asin").get();
    }

    /**
     * 解析商品价格
     */
    String getPrice(Selectable goodsNode) {
        String price = "";
        List<Selectable> priceNodes = goodsNode.xpath("//span[contains(@class, 'sx-price')]/*").nodes();
        for (Selectable priceNode : priceNodes) {
            String classStr = priceNode.xpath("/*/@class").get();
            if (classStr.contains("fractional")) {
                price += ("." + priceNode.xpath("/*/text()").get());
            } else {
                price += priceNode.xpath("/*/text()").get();
            }
        }
        return price;
    }

    /**
     * 解析商品跟卖数
     */
    int getOffersNum(Selectable goodsNode) {
        int offersNum = 0;
        List<String> resultList = goodsNode.regex("(\\d+) offer").all();
        for (String offer : resultList) {
            offersNum += Integer.valueOf(offer);
        }
        return offersNum;
    }

    /**
     * 解析排名商品的品类信息
     */
    String getDepartmentInfo(Selectable goodsNode) {
        String departmentInfo = null;
        departmentInfo = goodsNode.xpath("//a/span[@class='a-text-bold']").get();
        departmentInfo += goodsNode.xpath("//a[@class='a-size-small a-link-normal a-text-normal' and contains(@href,'/s/')]/text()").get();
        //departmentInfo += goodsNode.regex("(See.*?items)").get();
        return departmentInfo;
    }

    /**
     * 解析排名商品的发货方式
     */
    String getDeliveryMode(Selectable goodsNode) {
        String deliveryMode = goodsNode.xpath("//i[contains(@class, 'a-icon-prime')]/span/text()").get();
        if (StringUtils.isNotEmpty(deliveryMode)) {
            return deliveryMode.trim().equalsIgnoreCase("Prime") ? "Prime" : "Others";
        }
        return "Others";
    }

    /**
     * 解析排名商中的状态（Bestseller or Gesponsert）
     */
    String getGoodsStatus(Selectable goodsNode) {
        StringBuffer goodsStatus = new StringBuffer();
        String bestSeller = goodsNode.xpath("//span[contains(@class, 'sx-bestseller-color')]/span/text()").get();
        String sponsored = goodsNode.xpath("//h5[contains(@class, 's-sponsored-list-header')]/text()").get();
        if (StringUtils.isNotEmpty(bestSeller) && StringUtils.isNotEmpty(sponsored)) {
            goodsStatus.append(bestSeller.trim());
            goodsStatus.append(" and ");
            goodsStatus.append(sponsored.trim());
        } else if (StringUtils.isEmpty(bestSeller) && StringUtils.isEmpty(sponsored)) {
            return null;
        } else {
            goodsStatus.append(bestSeller);
            goodsStatus.append(sponsored);
        }

        return goodsStatus.toString().trim();
    }

    /**
     * 解析排名商品是否免运费
     */
    boolean getDistributionMode(Selectable goodsNode) {
        return goodsNode.regex("FREE Shipping").match();
    }

    /**
     * 解析URL页码数
     */
    Integer getUrlPage(String url) {
        Matcher matcher = Pattern.compile("page=(\\d+)&?").matcher(url);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group(1));
        } else {
            return null;
        }
    }

    /**
     * 解析商品排名
     */
    Integer getRankInfo(Selectable goodsNode) {
        String id = goodsNode.xpath("/li/@id").get();
        return Integer.valueOf(id.replace("result_", "").trim()) + 1;
    }

    /**
     * 解析通过关键词搜索到的商品的最大页码
     */
    Map<String, Integer> getHomePageInfo(Page page, List<Selectable> goodsNodesList) {
        Map<String, Integer> resultMap = new HashMap<String, Integer>();

        /*最大页数*/
        String maxPageNum = page.getHtml().xpath("//div[@id='pagn']//span[@class='pagnDisabled']/text()").get();
        if (StringUtils.isNotEmpty(maxPageNum)) {
            resultMap.put(R.KeywordRank.MAX_PAGE_NUM, Integer.valueOf(maxPageNum));
        } else {
            resultMap.put(R.KeywordRank.MAX_PAGE_NUM, 1);
        }
        /*第一页的商品数*/
        resultMap.put(R.KeywordRank.EVERY_PAGE_NUM, goodsNodesList.size());
        return resultMap;
    }

    /**
     * 判断是不是最后一页
     */
    boolean isLastPage(Page page) {
        return StringUtils.isEmpty(page.getHtml().xpath("//div[@id='pagn']/span[@class='pagnDisabled']").get());
    }

    /**
     * 拼接搜索URL
     */
    private String getUrl(RankSearchKeyword rankSearchKeyword) {
        String siteCode = rankSearchKeyword.getSiteCode();
        if (StringUtils.isEmpty(siteCode)) {
            return null;
        }
        String department = null;
        String keyword = null;
        try {
            department = URLEncoder.encode(rankSearchKeyword.getDepartment(), "UTF-8");
            keyword = URLEncoder.encode(rankSearchKeyword.getKeyword(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            sLogger.error(e);
        }
        if (siteCode.equalsIgnoreCase(US)) {
            return US + "/s?url=" + department + "keywords=" + keyword + "&ie=UTF8&lo=none";
        }
        return null;
    }

    @Override
    public us.codecraft.webmagic.Site getSite() {
        sLogger.info("getSite()::");
        mSite.setUserAgent(mUserAgentService.findRandomUA().userAgent).setAcceptStatCode(mDealSet);
        return mSite;
    }

    @Override
    public void execute() {
        /*查询需要监测的关键词信息*/
        List<RankSearchKeyword> rankSearchKeywordList = null;
        Url url;
        List<Url> urlList = new ArrayList<>();
        for (RankSearchKeyword rankSearchKeyword : rankSearchKeywordList) {
            url = new Url();
            url.url = getUrl(rankSearchKeyword);
            url.siteCode = rankSearchKeyword.getSiteCode();
            url.asin = rankSearchKeyword.getAsin();
            url.type = 5; /*关键词排名信息*/
            urlList.add(url);
        }
        startToCrawl(urlList);
    }

    void startToCrawl(List<Url> urlList) {
        sLogger.info("需要监测的关键词首页URL个数：" + urlList.size());
        if (CollectionUtils.isNotEmpty(urlList)) {

            Spider mSpider = Spider.create(this)
                    .setDownloader(mHttpClientImplDownloader)
                    .thread(1);

            for (Url url : urlList) {
                Request request = new Request(url.url);
                request.putExtra(URL_EXTRA, url);
                mSpider.addRequest(request);
                url.crawling = 1;
                mUrlService.update(url);
            }

            sLogger.info("开始爬取评论...");
            mSpider.start();
        }
    }
}