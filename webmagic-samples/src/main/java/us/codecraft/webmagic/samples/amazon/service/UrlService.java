package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.UrlDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param type 抓取类型。0-全量爬取；1-监控Review；2-更新爬取
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
        List<Url> historyList = mHistoryService.findByAsin(url.saaAsin);

        mLogger.info("ASIN:" + url.saaAsin + " 对应的URL表记录数：" + list.size() + " 历史表记录数：" + historyList.size());

        list.addAll(historyList);

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

        //数据统计开始============================================================================
        /* 找出评论的最大页码 */
        int maxPage = 0;
        for (Integer filterMaxPage : maxPageMap.values()) {
            /* 总的需要爬取的页码为每个过滤器最大页码的总和 */
            maxPage += filterMaxPage;
        }

        /*
        * 如果 当前ASIN的URL列表集合数量 = 最大页码，表示已经爬取完毕了
        */
        mLogger.info("最大页码总数：" + maxPage + " 已经爬取的页码：" + crawledList.size());
        Asin asinObj = mAsinService.findByAsin(url.saaAsin);
        if (crawledList.size() == maxPage) {
            /*
            * 全量爬取完毕，把需要爬取星级的最后一条评论时间记录到extra字段，方便下次更新爬取的时候使用
            */
            mAsinService.updateAsinStat(asinObj);
        } else {
            float progress = 1.0f * crawledList.size() / maxPage;
            asinObj.saaProgress = progress > 1.0f ? 1.0f : progress;
            mLogger.info(url.saaAsin + " 的爬取进度：" + asinObj.saaProgress);
            mAsinService.updateStatus(asinObj, false);
        }
        //数据统计结束============================================================================

        //数据更改开始============================================================================
        /* 用过滤器作key，过滤器对应已经爬取的Url列表做value */
        Map<String, List<Url>> filterUrlMap = new HashMap<String, List<Url>>();

        for (Url urlLooper : crawledList) {

            String filter = UrlUtils.getValue(urlLooper.url, "filterByStar");
            List<Url> urlList = filterUrlMap.get(filter);

            if (urlList == null) {
                urlList = new ArrayList<Url>();
                filterUrlMap.put(filter, urlList);
            }

            urlList.add(urlLooper);
        }

        mLogger.info("过滤器对应对应已爬取URL列表：" + filterUrlMap);

        for (String filter : maxPageMap.keySet()) {

            Integer filterMaxPageNum = maxPageMap.get(filter);
            List<Url> filterUrlList = filterUrlMap.get(filter);

            if (filterUrlList == null) {
                filterUrlList = new ArrayList<Url>();
            }

            int filterCrawledPage = filterUrlList.size();

            if (filterCrawledPage == filterMaxPageNum) {
                /* 全量爬取完成后，删除全量爬取的URL，并把它记入URL历史表 */
                deleteAll(crawledList);
                mHistoryService.addAll(crawledList);
            }

        }
        //数据更改结束============================================================================

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

        /* 删除后再检查一次更新爬取的Url是否为空，如果为空，就标记
        该ASIN的更新爬取状态为0，表示可以继续下一次的更新爬取了 */
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
}
