package us.codecraft.webmagic.samples.amazon.service;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.UrlDao;
import us.codecraft.webmagic.samples.amazon.pojo.*;
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
    private ReviewService mReviewService;
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
    public void updateAsinCrawledAll(String asin) {

        List<Url> list = mUrlDao.findByAsin(asin);

        /*
        *  把状态码已经为200的加入到临时集合
        */
        List<Url> crawledList = new ArrayList<Url>();
        for (Url url : list) {
            if (url.status == 200) {
                crawledList.add(url);
            }
        }

        /*
        * 找出评论的最大页码
        */
        int maxPage = 0;
        if (CollectionUtils.isNotEmpty(list)) {
            for (Url url : list) {
                String tmp = UrlUtils.getValue(url.url, "pageNumber");
                int current = StringUtils.isEmpty(tmp) ? 1 : Integer.valueOf(tmp);
                if (current > maxPage) {
                    maxPage = current;
                }
            }
        }

        /*
        * 如果 当前ASIN，URL列表集合数量 < 最大页码，表示已经爬取完毕了
        */
        mLogger.info("最大页码：" + maxPage + " 已经爬取的页码：" + crawledList.size());
        Asin asinObj = mAsinService.findByAsin(asin);
        if (crawledList.size() == maxPage) {

            /*
            * 全量爬取完毕，把需要爬取星级的最后一条评论时间记录到extra字段，方便下次更新爬取的时候使用
            */
            List<Review> reviewList = mReviewService.findLastReview(asin);

            /*
            * 取出该ASIN每个星级对应评论总数，加入到Map集合，方便下面的循环读取
            */
            List<StarReviewCount> srcList = mReviewService.findStarReviewCount(asin);
            Map<Integer, Integer> srcMap = new HashMap<Integer, Integer>();
            for (StarReviewCount src : srcList) {
                srcMap.put(src.star, src.count);
            }

            if (CollectionUtils.isNotEmpty(reviewList)) {
                List<StarReviewMap> mapList = new ArrayList<StarReviewMap>();
                for (Review review : reviewList) {
                    StarReviewMap sRMap = new StarReviewMap(review.sarStar, review.sarReviewId);
                    sRMap.reviewNum = srcMap.get(review.sarStar);
                    mapList.add(sRMap);
                }
                asinObj.extra = new Gson().toJson(mapList);
            }

            /*
            * 标记该ASIN为已经爬取完毕
            */
            mAsinService.updateStatus(asinObj, true);


            /*
            * 全量爬取完成后，删除全量爬取的URL，并把它记入URL历史表
            */
            mUrlDao.deleteByAsin(asin);
            mHistoryService.addAll(crawledList);

        } else {
            if (maxPage != 0) {
                float progress = 1.0f * crawledList.size() / maxPage;
                asinObj.saaProgress = progress > 1.0f ? 1.0f : progress;
                mLogger.info(asin + " 的爬取进度：" + asinObj.saaProgress);
                mAsinService.updateStatus(asinObj, false);
            }
        }
    }

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
}
