package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.UrlDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.BatchAsin;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

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

    private Logger mLogger = Logger.getLogger(getClass());

    /**
     * 只做添加Url，若对象的url字段已经存在，则不进行任何处理
     */
    public void add(Url url) {
        if (!isExist(url.urlMD5)) {
            mUrlDao.add(url);
        }
    }

    public void deleteByAsin(String asin) {
        mUrlDao.deleteByAsin(asin);
    }

    public void update(Url url) {
        mUrlDao.update(url);
    }

    public void addAll(List<Url> urlList) {

        List<Url> newList = new ArrayList<Url>();

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

    /**
     * 根据Url最大页码和已经爬取过的页码判断是否已经爬取完毕
     * yes：把asin的状态更新到爬取完毕，no：ignore
     */
    public void updateAsinCrawledAll(Url url) {
        List<Url> list = mUrlDao.findByAsin(url.saaAsin);

        mLogger.info("ASIN:" + url.saaAsin + " 对应的URL表记录数：" + list.size());

        /* 用过滤器作key，过滤器对应的最大的评论页码作value */
        Map<String, Integer> maxPageMap = new HashMap<String, Integer>();

        for (Url urlLooper : list) {

            String filter = UrlUtils.getValue(urlLooper.url, "filterByStar");
            Integer pn = maxPageMap.get(filter);

            if (pn == null) {
                pn = 1;
            }

            String pageNumStr = UrlUtils.getValue(urlLooper.url, "pageNumber");
            Integer pageNum = StringUtils.isEmpty(pageNumStr) ? 1 : Integer.valueOf(pageNumStr);

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
        Asin asinObj = mAsinService.findByAsin(url.saaAsin);
        List<BatchAsin> batchAsinList = mBatchAsinService.findAllByAsin(url.saaAsin);
        /* 先把rootAsin值填进去 */
        for (BatchAsin batchAsin : batchAsinList) {
            batchAsin.rootAsin = asinObj.saaRootAsin;
        }

        /* 如果 当前ASIN，URL列表集合数量 = 最大页码，表示已经爬取完毕了 */
        if (crawledList.size() == maxPage) {

            /* 更新ASIN的extra状态 */
            Asin asin = mAsinService.updateExtra(asinObj);

            /* 更新ASIN的爬取状态状态 */
            mAsinService.updateStatus(asinObj, true);

            /* 爬取完毕，把所有URL移动到历史表 */
            deleteAll(crawledList);
            mHistoryService.addAll(crawledList);

            /* 二期业务：改变详单表extra状态进行更新 */
            for (BatchAsin batchAsin : batchAsinList) {
                batchAsin.extra = asin.extra;
                batchAsin.progress = 1;
            }
        } else {
            /* 一期业务 */
            float progress = 1.0f * crawledList.size() / maxPage;
            asinObj.saaProgress = progress > 1.0f ? 1.0f : progress;
            mLogger.info(url.saaAsin + " 的爬取进度：" + asinObj.saaProgress);

            mAsinService.updateStatus(asinObj, false);

            /* 二期业务：改变详单表进度状态进行更新 */
            for (BatchAsin batchAsin : batchAsinList) {
                batchAsin.progress = asinObj.saaProgress;
            }
        }
        /* 更新批次详单表 */
        mBatchAsinService.updateAll(batchAsinList);

        /* 二期业务：更新批次单表 */
        for (BatchAsin batchAsin : batchAsinList) {
            /* 对批次详单进行更新 */
            if(batchAsin.progress == 1){
                batchAsin.finishTime = new Date();
                /* 调整为更新爬取 */
                batchAsin.type = 1;
            }
            if(batchAsin.startTime == null){
                batchAsin.startTime = new Date();
            }

            /* 对批次单进行更新 */
            List<BatchAsin> batchAsinList2 = mBatchAsinService.findAllByBatchNum(batchAsin.batchNumber);

            float progress = 0;
            /* 计算总的进度值之和 */
            for (BatchAsin batchAsin2 : batchAsinList2) {
                progress += batchAsin2.progress;
            }

            /* 计算订单平均进度值 */
            progress = progress / batchAsinList2.size();

            Batch batch = mBatchService.findByBatchNumber(batchAsin.batchNumber);
            batch.progress = progress;

            /* 如果爬取开始，更新开始时间 */
            if (batch.startTime == null) {
                batch.startTime = new Date();
            }
            /* 如果爬取完毕，更新完毕时间 */
            if (progress == 1) {
                batch.finishTime = new Date();
            }


            mBatchService.update(batch);

        }

        /* 再次更新批次详单表 */
        mBatchAsinService.updateAll(batchAsinList);
    }

    /**
     * 查询未在爬取中 & 监听爬取类型 & 离上次处理大于一定时间间隔 的记录。
     * 1，其中时间间隔按照优先级大小+1后诚意300秒来计算的，eg：priority=0的，
     * 5分钟会爬取监听一次；
     * 2，记录已经按照爬取次数和优先级的升序排序了，这样就不会导致低优先级的
     * ，高监听次数的永远再监听不到了。
     */
    public List<Url> findMonitorUrlList() {
        return mUrlDao.findMonitorUrlList();
    }

    /**
     * 某个星级的更新爬取完毕，删除该星级该过滤器的所有更新爬取的链接
     *
     * @param asin   ASIN码
     * @param filter Url中的过滤器
     */
    public void deleteUpdateCrawl(String asin, String filter) {
        List<Url> list = findUpdateCrawl(asin);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Url url : list) {
                if (url.url.contains(filter)) {
                    mUrlDao.delete(url.id);
                }
            }
        }

        /*删除后再检查一次更新爬取的Url是否为空，如果为空，就标记
        该ASIN的更新爬取状态为0，表示可以继续下一次的更新爬取了*/
        if (CollectionUtils.isEmpty(findUpdateCrawl(asin))) {
            Asin byAsin = mAsinService.findByAsin(asin);
            byAsin.saaIsUpdatting = 0;
            mAsinService.udpate(byAsin);
        }
    }

    public List<Url> findUpdateCrawl(String asin) {
        return mUrlDao.findUpdateCrawl(asin);
    }

    public boolean isExist(String urlMd5) {
        return mUrlDao.find(urlMd5).size() > 0;
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
            mUrlDao.deleteByUrlMd5(url.urlMD5);
        }
    }

    /**
     * 删除正在更新爬取的URL
     */
    public void deleteUpdating() {
        mUrlDao.deleteByType(2);
    }

    /**
     * 切换所有ASIN的URL的优先级
     */
    public void updatePriority(String asin, int priority) {
        mUrlDao.updatePriority(asin, priority);
    }
}
