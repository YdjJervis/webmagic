package com.eccang.spider.amazon.service;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.dao.UrlDao;
import com.eccang.spider.amazon.pojo.Asin;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.pojo.batch.BatchAsinExtra;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.service.batch.BatchAsinService;
import com.eccang.spider.amazon.service.batch.BatchService;
import com.eccang.spider.amazon.service.crawl.ReviewService;
import com.eccang.spider.amazon.service.relation.AsinRootAsinService;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;
import com.eccang.spider.base.util.UrlUtils;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Url爬取队列业务
 * @date 2016/10/11
 */
@Service
public class UrlService {

    @Autowired
    private UrlDao mUrlDao;
    @Autowired
    private AsinService mAsinService;

    @Autowired
    private UrlHistoryService mHistoryService;

    @Autowired
    private BatchAsinService mBatchAsinService;

    @Autowired
    private BatchService mBatchService;

    @Autowired
    private AsinRootAsinService mAsinRootAsinService;

    @Autowired
    private CustomerAsinService mCustomerAsinService;

    @Autowired
    private PushQueueService mPushQueueService;

    @Autowired
    private ReviewService mReviewService;

    private Logger mLogger = Logger.getLogger(getClass());


    /**
     * 只做添加Url，若对象的url字段已经存在，则不进行任何处理
     */
    public void add(Url url) {
        if (!isExist(url.urlMD5)) {
            mUrlDao.add(url);
        }
    }

    public void deleteByAsin(String siteCode, String asin) {
        mUrlDao.deleteByAsin(siteCode, asin);
    }

    public void deleteByUrlMd5(String urlMd5) {
        mUrlDao.deleteByUrlMd5(urlMd5);
    }

    public Url findByUrlMd5(String urlMd5) {
        return mUrlDao.findByUrlMd5(urlMd5);
    }

    public void update(Url url) {
        mUrlDao.update(url);
    }

    public void addAll(List<Url> urlList) {

        List<Url> newList = new ArrayList<>();

        for (Url url : urlList) {
            if (!isExist(url.urlMD5)) {
                newList.add(url);
            } else {
                mUrlDao.update(url);
            }
        }

        if (CollectionUtils.isNotEmpty(newList)) {
            mUrlDao.addAll(newList);
        }

    }

    /**
     * @param type 抓取类型。0-抓Review
     * @return 状态码不为200的所有Url
     */
    public List<Url> find(int type) {
        return mUrlDao.findByType(type);
    }

    public List<Url> findTop100() {
        return mUrlDao.findTop100();
    }

    public List<Url> find(String batchNum, String siteCode, String asin, int type) {
        return mUrlDao.find(batchNum, siteCode, asin, type);
    }

    /**
     * 根据Url最大页码和已经爬取过的页码判断是否已经爬取完毕
     * yes：把asin的状态更新到爬取完毕，no：ignore
     */
    public void updateAsinCrawledAll(Url url) {

        List<Url> list = find(url.batchNum, url.siteCode, url.asin, R.CrawlType.REVIEW_ALL);

        mLogger.info("ASIN:" + url.asin + " 对应的URL表记录数：" + list.size());

        /* 如果暂时不能计算总进度，就返回 */
        if (!canCalculateProgress(list)) {
            mLogger.info(url.asin + " 暂时不能统计进度");
            return;
        }

        /* 用过滤器作key，过滤器对应的最大的评论页码作value */
        Map<String, Integer> maxPageMap = new HashMap<>();
        for (Url urlLooper : list) {

            String filter = UrlUtils.getValue(urlLooper.url, "filterByStar");
            Integer pn = maxPageMap.get(filter);

            if (pn == null) {
                pn = 1;
            }

            int pageNum = getPageNum(urlLooper.url);

            if (pageNum >= pn) {
                maxPageMap.put(filter, pageNum);
            }

        }
        mLogger.info("过滤器对应页码：" + maxPageMap);

        /* 把状态码已经为200的加入到临时集合 */
        List<Url> crawledList = new ArrayList<Url>();
        for (Url urlLoop : list) {
            if (urlLoop.status == 200) {
                crawledList.add(urlLoop);
            }
        }

        /* 找出评论的最大页码 */
        int maxPage = 0;
        for (Integer filterMaxPage : maxPageMap.values()) {
            /* 总的需要爬取的页码为每个过滤器最大页码的总和 */
            maxPage += filterMaxPage;
        }

        mLogger.info("最大页码总数：" + maxPage + " 已经爬取的页码：" + crawledList.size());

        /* 从数据库获取一些必要对象 */
        Asin asinObj = mAsinService.findByAsin(url.siteCode, mAsinRootAsinService.findByAsin(url.asin, url.siteCode).rootAsin);
        Batch batch = mBatchService.findByBatchNumber(url.batchNum);
        BatchAsin dbBtchAsin = mBatchAsinService.findAllByAsin(url.batchNum, url.siteCode, url.asin);
        Date currentTime = new Date();

        /* 更新几个变量值 */
        dbBtchAsin.rootAsin = asinObj.rootAsin;
        if (dbBtchAsin.startTime == null) {
            dbBtchAsin.startTime = currentTime;
        }

        /* 如果 当前ASIN，URL列表集合数量 = 最大页码，表示已经爬取完毕了 */
        if (crawledList.size() == maxPage) {

            /* 爬取完毕，把所有URL移动到历史表 */
            deleteAll(crawledList);
            mHistoryService.addAll(crawledList);

            /* 更新ASIN的extra状态 */
            Asin asin = mAsinService.updateExtra(asinObj);

            /* 二期业务：更新详单表字段 */
            dbBtchAsin.extra = getBatchAsinExtra(dbBtchAsin, asin.rootAsin);
            dbBtchAsin.finishTime = currentTime;
            dbBtchAsin.progress = 1;
            dbBtchAsin.status = 4;

            /* 更新客户-Asin关系的同步时间 */
            mLogger.info("同步客户关系表记录时间：" + batch.customerCode + " " + dbBtchAsin.siteCode + " " + dbBtchAsin.asin);
            CustomerAsin customerAsin = mCustomerAsinService.find(new CustomerAsin(batch.customerCode, dbBtchAsin.siteCode, dbBtchAsin.asin));
            customerAsin.syncTime = currentTime;
            mCustomerAsinService.update(customerAsin);

        } else {
            /* 二期业务：改变详单表单条记录的进度状态 */
            dbBtchAsin.progress = 1.0f * crawledList.size() / maxPage;
        }

        /* 更新批次详单表 */
        mBatchAsinService.update(dbBtchAsin);

        /*如果爬取开始，更新开始时间 */
        if (batch.startTime == null) {
            batch.startTime = currentTime;
        }

        /*计算详单总进度的评价值，更新到总单上去 */
        batch.progress = mBatchAsinService.findAverageProgress(url.batchNum);

        /*如果爬取完毕，更新完毕时间 */
        if (batch.progress == 1) {
            batch.finishTime = currentTime;
            batch.status = 2;

            /* 全量爬取完毕，放进推送队列 */
            mPushQueueService.add(batch.number);
        }

        mBatchService.update(batch);

    }

    /**
     * @param batchAsin 单条批次详单
     * @param rootAsin  根ASIN码
     * @return 大字段数据
     */
    public String getBatchAsinExtra(BatchAsin batchAsin, String rootAsin) {
        List<BatchAsinExtra> batchAsinExtraList = new ArrayList<BatchAsinExtra>();
        List<Integer> starList = mBatchAsinService.getStarArray(batchAsin.star);
        for (Integer star : starList) {
            batchAsinExtraList.addAll(mReviewService.findAll(rootAsin, star));
        }
        return new Gson().toJson(batchAsinExtraList);
    }


    /**
     * 包含所有过滤器的Url列表中，如果页码为空的那一页恰好状态码不为200，
     * 说明没有爬取过那个过滤器的第一页，就没法计算所有过滤器的总页码，
     * 也就没法统计总进度，那就不去统了。
     */
    private boolean canCalculateProgress(List<Url> list) {
        for (Url url : list) {
            if (getPageNum(url.url) == 1 && url.status != 200) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return 页码
     */
    public int getPageNum(String url) {
        String pageNumStr = UrlUtils.getValue(url, "pageNumber");
        return StringUtils.isEmpty(pageNumStr) ? 1 : Integer.valueOf(pageNumStr);
    }

    public boolean isExist(String urlMd5) {
        return mUrlDao.findByUrlMD5Count(urlMd5) > 0;
    }

    /**
     * 重置URL状态。每次强行停止的时候，很多URL都会是正在爬取的状态，
     * 导致下次启动的时候就取不到这些未爬取的URL了，所以每次开启项目
     * 的时候都重置一下状态就可以了。
     */
    public void resetStatus() {
        mUrlDao.resetStatus();
    }

    public void deleteAll(List<Url> urlList) {
        for (Url url : urlList) {
            deleteByUrlMd5(url.urlMD5);
        }
    }

    /**
     * 删除正在更新爬取的URL
     */
    public void deleteUpdating() {
        mUrlDao.deleteByType(2);
    }

    public List<Url> findByBatchNum(String batchNumber) {
        return mUrlDao.findByBatchNum(batchNumber);
    }

    public List<Url> findByBatchNumAndSite(String batchNum, String siteCode) {
        return mUrlDao.findByBatchNumAndSite(batchNum, siteCode);
    }

    /**
     * 根据批次号与站点码删除url
     */
    public void deleteByBNSC(String batchNum, String siteCode) {
        mUrlDao.deleteByBNSC(batchNum, siteCode);
    }
}
