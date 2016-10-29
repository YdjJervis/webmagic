package us.codecraft.webmagic.samples.amazon.service;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.UrlDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.StarReviewMap;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

import java.util.ArrayList;
import java.util.List;

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

    private Logger mLogger = Logger.getLogger(getClass());

    /**
     * 只做添加Url，若对象的url字段已经存在，则不进行任何处理
     */
    public void add(Url url) {
        mUrlDao.add(url);
    }

    public void deleteByAsin(String asin) {
        mUrlDao.deleteByAsin(asin);
    }

    public void update(Url url) {
        mUrlDao.update(url);
    }

    public void addAll(List<Url> urlList) {

        if (CollectionUtils.isEmpty(urlList)) return;

        for (Url url : urlList) {
            add(url);
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
        mLogger.info("最大页码：" + maxPage + " 已经爬取的页码：" + list.size());
        Asin asinObj = mAsinService.findByAsin(asin);
        if (list.size() == maxPage) {

            /*
            * 全量爬取完毕，把需要爬取星级的最后一条评论时间记录到extra字段，方便下次更新爬取的时候使用
            */
            List<Review> reviewList = mReviewService.findLastReview(asin);
            if (CollectionUtils.isNotEmpty(reviewList)) {
                List<StarReviewMap> mapList = new ArrayList<StarReviewMap>();
                for (Review review : reviewList) {
                    mapList.add(new StarReviewMap(review.sarStar, review.sarReviewId));
                }
                asinObj.extra = new Gson().toJson(mapList);
            }

            /*
            * 标记该ASIN为已经爬取完毕
            */
            mAsinService.updateStatus(asinObj, true);

        } else {
            if (maxPage != 0) {
                float progress = 1.0f * list.size() / maxPage;
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

}
