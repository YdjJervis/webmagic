package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.downloader.HttpClientImplDownloader;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.GoodsRankInfo;
import us.codecraft.webmagic.samples.amazon.pojo.KeywordRank;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.pojo.batch.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchRank;
import us.codecraft.webmagic.samples.amazon.service.*;
import us.codecraft.webmagic.samples.amazon.service.batch.BatchRankService;
import us.codecraft.webmagic.samples.amazon.service.batch.BatchService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;
import us.codecraft.webmagic.samples.base.service.UserAgentService;
import us.codecraft.webmagic.samples.base.util.UrlUtils;
import us.codecraft.webmagic.selector.Selectable;

import java.net.URLDecoder;
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
public class KeywordRankProcessor extends BasePageProcessor implements ScheduledTask {

    Logger sLogger = Logger.getLogger(getClass());
    private static final String US = "https://www.amazon.com";

    @Autowired
    UserAgentService mUserAgentService;
    @Autowired
    KeywordRankService mKeywordRankService;
    @Autowired
    GoodsRankInfoService mGoodsRankInfoService;
    @Autowired
    BatchRankService mBatchRankService;
    @Autowired
    BatchService mBatchService;
    @Autowired
    PushQueueService mPushQueueService;
    @Autowired
    UrlService mUrlService;
    @Autowired
    UrlHistoryService mUrlHistoryService;
    @Autowired
    private HttpClientImplDownloader mHttpClientImplDownloader;

    @Override
    protected void dealOtherPage(Page page) {
        boolean isFind = false;
        Url url = (Url) page.getRequest().getExtra(URL_EXTRA);
        /*设置Batch总单的开始时间*/
        setBatchStartTime(url);
        /*获取当前页的商品结点列表*/
        List<Selectable> goodsNodesList = extractGoodsNodesList(page);

        String departmentCode = URLDecoder.decode(UrlUtils.getValue(url.url, R.KeywordRank.DEPARTMENT));
        String keyword = URLDecoder.decode(UrlUtils.getValue(url.url, R.KeywordRank.KEYWORDS));
        String asin = url.asin;
        String siteCode = url.siteCode;
        KeywordRank keywordRank = new KeywordRank(asin, keyword, siteCode, departmentCode);
        /*判断是否是首页*/
        if (isHomePage(url.url)) {
            getRankTop10GoodsInfo(goodsNodesList, keywordRank, url);
            Map<String, Integer> homePage = getHomePageInfo(page, goodsNodesList);
            KeywordRank rank = mKeywordRankService.findByObj(keywordRank);
            if (rank == null) {
                /*将最大页数与每页的商品数更新到监测表中*/
                keywordRank.setTotalPages(homePage.get(R.KeywordRank.MAX_PAGE_NUM));
                keywordRank.setEveryPage(homePage.get(R.KeywordRank.EVERY_PAGE_NUM));
                mKeywordRankService.add(keywordRank);
            } else {
                rank.setTotalPages(homePage.get(R.KeywordRank.MAX_PAGE_NUM));
                rank.setEveryPage(homePage.get(R.KeywordRank.EVERY_PAGE_NUM));
                mKeywordRankService.updateByObj(rank);
            }
        }

        int rankNum = 0;
        /*查询客户商品在对应的关键词下的搜索排名*/
        if (CollectionUtils.isNotEmpty(goodsNodesList)) {
            for (Selectable goodsNode : goodsNodesList) {
                /*获取排名*/
                rankNum = getRankInfo(goodsNode);
                /*解析商品ASIN*/
                String goodsAsin = getGoodsAsin(goodsNode);
                /*搜索到100名还没找到对应的客户商品，将停止搜索*/
                if (rankNum > 100) {
                    sLogger.info("搜索前100名，没有找到对应的商品.");
                    break;
                }
                /*当前商品与客户监测的商品ASIN一致则找到其排名，停止搜索*/
                if (goodsAsin.equalsIgnoreCase(asin)) {
                    isFind = true;
                    break;
                }
            }
        }

        /*关键词搜索完成：1.找到对应的asin，2：没有找到，但是排名在100以后，3.没有找到，并搜到的商品数小于100*/
        if (isFind || rankNum > 100 || isLastPage(page)) {
            /*查询关键词排名信息*/
            KeywordRank rank = mKeywordRankService.findByObj(keywordRank);
            boolean isChanged = false;
            if (rank.getRankNum() != rankNum) {
                isChanged = true;
            }
            /*更新搜索关键词排名状态*/
            keywordRank.setRankNum(rankNum);
            mKeywordRankService.updateRankNum(keywordRank);
            /*更新详单已经完成*/
            updateBatchStatus(url, keywordRank, isChanged);
        } else {
            /*将下一页的URL放入到URL表中*/
            addNextPage2Url(url);
        }
    }

    /**
     * 设置Batch总单的开始时间
     */
    private void setBatchStartTime(Url url) {
        Batch batch = mBatchService.findByBatchNumber(url.batchNum);
        if (batch.startTime == null) {
            batch.startTime = new Date();
        }
        mBatchService.update(batch);
    }

    /**
     * 将下一页添加到URL数据库表中
     */
    private void addNextPage2Url(Url url) {
        Integer pageNum = getUrlPage(url.url);
        if (pageNum == null) {
            url.parentUrl = url.url;
            url.url = url.url + "&page=2";
        } else {
            pageNum += 1;
            url.url = url.parentUrl + "&page=" + pageNum;
        }
        url.urlMD5 = UrlUtils.md5(url.batchNum + url.url);
        url.status = 0;
        url.times = 0;
        mUrlService.add(url);
    }

    /**
     * 更新批次状态
     */
    private void updateBatchStatus(Url url, KeywordRank keywordRank, boolean isChanged) {
        BatchRank batchRank = initBatchRank(url, keywordRank);
        String batchNum = url.batchNum;
        /*查询详单信息*/
        batchRank = mBatchRankService.findByObj(batchRank);
        batchRank.setStatus(2);
        batchRank.setIsChanged(isChanged ? 1 : 0);
        mBatchRankService.update(batchRank);

        float progress = mBatchRankService.findAverageProgress(batchNum);
        Batch batch = mBatchService.findByBatchNumber(batchNum);
        if (progress == 1) {
            batch.finishTime = new Date();
            batch.status = 2;
            /*添加到需要推送队列表中*/
            mPushQueueService.add(batchNum);

            /*将URL从url表转到url_history表中*/
            List<Url> finishUrls = mUrlService.findByBatchNum(url.batchNum);
            mUrlHistoryService.addAll(finishUrls);
            mUrlService.deleteAll(finishUrls);
        } else {
            batch.status = 1;
        }
        batch.progress = progress;
        mBatchService.update(batch);
    }

    /**
     * 初始化关键词排名详单对象
     */
    private BatchRank initBatchRank(Url url, KeywordRank keywordRank) {
        BatchRank batchRank = new BatchRank();
        batchRank.setBatchNum(url.batchNum);
        batchRank.setAsin(keywordRank.getAsin());
        batchRank.setKeyword(keywordRank.getKeyword());
        batchRank.setSiteCode(keywordRank.getSiteCode());
        batchRank.setDepartmentCode(keywordRank.getDepartmentCode());
        return batchRank;
    }

    /**
     * 解析排名前十商品信息，并入库
     */
    private void getRankTop10GoodsInfo(List<Selectable> goodsNodesList, KeywordRank info, Url url) {
        List<GoodsRankInfo> top10GoodsInfos = new ArrayList<>();
        int rankIndex = 0;
            /*解析前十的商品信息*/
        for (Selectable goodsNode : goodsNodesList) {
            rankIndex++;
            GoodsRankInfo goodsRankInfo = extractGoodsNode(goodsNode);
            if (StringUtils.isEmpty(goodsRankInfo.getTitle())) {
                /*取前十商品信息，不含品类推荐*/
                rankIndex--;
                continue;
            }
            goodsRankInfo.setBatchNum(url.batchNum);
            goodsRankInfo.setAsin(info.getAsin());
            goodsRankInfo.setKeyword(info.getKeyword());
            goodsRankInfo.setSiteCode(info.getSiteCode());
            goodsRankInfo.setRankNum(rankIndex);
            goodsRankInfo.setDepartmentCode(info.getDepartmentCode());
            top10GoodsInfos.add(goodsRankInfo);
            if (rankIndex == 10) {
                break;
            }
        }
        if (CollectionUtils.isNotEmpty(top10GoodsInfos)) {
            /*将排名前十的商品信息入库*/
            mGoodsRankInfoService.addAll(top10GoodsInfos);
        }
    }

    /**
     * 解析商品排行信息列表
     */
    private List<Selectable> extractGoodsNodesList(Page page) {
        return page.getHtml().xpath("//*[@id=atfResults]/ul/li | //*[@id=btfResults]/ul/li").nodes();
    }

    /*
     * 判断商品是不是广告
     */
//    boolean isAdvert(Selectable goodsNode) {
//        String ad = goodsNode.xpath("//h5[contains(@class, 'contains')]").get();
//        return StringUtils.isNotEmpty(ad);
//    }

    /**
     * 判断是否是第一页
     */
    private boolean isHomePage(String url) {
        return !url.contains("page");
    }

    /**
     * 解析关键词搜索得到的商品信息
     */
    private GoodsRankInfo extractGoodsNode(Selectable goodsNode) {
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
        goodsRankInfo.setDeliveryMode(deliveryMode);
        goodsRankInfo.setDistributionMode(distributionMode);
        goodsRankInfo.setGoodsPictureUrl(pictureUrl);
        goodsRankInfo.setOffersNum(offersNum);
        goodsRankInfo.setDepartmentInfo(departmentInfo);
        goodsRankInfo.setGoodsStatus(goodsStatus);
        return goodsRankInfo;
    }

    /**
     * 解析商品ASIN
     */
    private String getGoodsAsin(Selectable goodsNode) {
        return goodsNode.xpath("/li/@data-asin").get();
    }

    /**
     * 解析商品价格
     */
    private String getPrice(Selectable goodsNode) {
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
    private int getOffersNum(Selectable goodsNode) {
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
    private String getDepartmentInfo(Selectable goodsNode) {
        String departmentInfo;
        departmentInfo = goodsNode.xpath("//a/span[@class='a-text-bold']/text()").get();
        departmentInfo += goodsNode.xpath("//a[@class='a-size-small a-link-normal a-text-normal' and contains(@href,'/s/')]/text()").get();
        //departmentInfo += goodsNode.regex("(See.*?items)").get();
        return departmentInfo;
    }

    /**
     * 解析排名商品的发货方式
     */
    private String getDeliveryMode(Selectable goodsNode) {
        String deliveryMode = goodsNode.xpath("//i[contains(@class, 'a-icon-prime')]/span/text()").get();
        if (StringUtils.isNotEmpty(deliveryMode)) {
            return deliveryMode.trim().equalsIgnoreCase("Prime") ? "Prime" : "Others";
        }
        return "Others";
    }

    /**
     * 解析排名商中的状态（Bestseller or Gesponsert）
     */
    private String getGoodsStatus(Selectable goodsNode) {
        StringBuilder goodsStatus = new StringBuilder();
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
    private boolean getDistributionMode(Selectable goodsNode) {
        return goodsNode.regex("FREE Shipping").match();
    }

    /**
     * 解析URL页码数
     */
    private Integer getUrlPage(String url) {
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
    private Integer getRankInfo(Selectable goodsNode) {
        String id = goodsNode.xpath("/li/@id").get();
        return Integer.valueOf(id.replace("result_", "").trim()) + 1;
    }

    /**
     * 解析通过关键词搜索到的商品的最大页码
     */
    private Map<String, Integer> getHomePageInfo(Page page, List<Selectable> goodsNodesList) {
        Map<String, Integer> resultMap = new HashMap<>();

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
    private boolean isLastPage(Page page) {
        return StringUtils.isEmpty(page.getHtml().xpath("//div[@id='pagn']/span[@class='pagnDisabled']").get());
    }

    @Override
    public void execute() {
        /*查询需要监测的关键词信息*/
        sLogger.info("开始执行关键词排名爬取任务...");
        List<Url> urls = mUrlService.find(R.CrawlType.KEYWORD_RANK);
        startToCrawl(urls);
    }
}