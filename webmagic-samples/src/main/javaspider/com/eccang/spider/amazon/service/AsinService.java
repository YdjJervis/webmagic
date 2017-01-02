package com.eccang.spider.amazon.service;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.AsinDao;
import com.eccang.spider.amazon.pojo.Asin;
import com.eccang.spider.amazon.pojo.crawl.Review;
import com.eccang.spider.amazon.pojo.StarReviewCount;
import com.eccang.spider.amazon.pojo.StarReviewMap;
import com.eccang.spider.amazon.service.crawl.ReviewService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Asin业务
 * @date 2016/10/11
 */
@Service
public class AsinService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private AsinDao mAsinDao;

    @Autowired
    private UrlService mUrlService;

    @Autowired
    private ReviewService mReviewService;

    /**
     * @return 未转换成URL的ASIN对象列表
     */
    public List<Asin> findAll() {
        return mAsinDao.findAll();
    }

    public Asin findByAsin(String siteCode, String asin) {
        return mAsinDao.findByAsin(siteCode, asin);
    }

    public Asin updateExtra(Asin asin) {

        asin.extra = getAsinExtra(asin.rootAsin);
        update(asin);

        return asin;
    }

    /**
     * 生成Asin表记录的Extra字段
     */
    private String getAsinExtra(String rootAsin) {
        /* 全量爬取完毕，把需要爬取星级的最后一条评论ReviewID记录到extra字段，方便下次更新爬取的时候使用 */
        List<Review> reviewList = mReviewService.findLastReview(rootAsin);

        /* 取出该ASIN每个星级对应评论总数，加入到Map集合，方便下面的循环读取 */
        List<StarReviewCount> srcList = mReviewService.findStarReviewCount(rootAsin);

        /* List转Map */
        Map<Integer, Integer> srcMap = new HashMap<Integer, Integer>();
        for (StarReviewCount src : srcList) {
            srcMap.put(src.star, src.count);
        }

        List<StarReviewMap> mapList = new ArrayList<StarReviewMap>();
        for (Review review : reviewList) {
            StarReviewMap sRMap = new StarReviewMap(review.star, review.reviewId);
            sRMap.reviewNum = srcMap.get(review.star);
            mapList.add(sRMap);
        }

        return new Gson().toJson(mapList);
    }

    public void update(Asin asin) {
        mAsinDao.update(asin);
    }

    public void updateAndDeleteUrl(String siteCode, String asinCode) {
        mLogger.warn("该商品已经下架：" + asinCode);

        /*标记此ASIN为下架产品*/
        Asin asin = findByAsin(siteCode, asinCode);
        update(asin);
        /*删除已经在爬取的URL*/
        mUrlService.deleteByAsin(siteCode, asinCode);
    }

    /**
     * @param star 需要抓取的等级。eg：0-0-1-1-1表示需要抓取差评
     * @return 亚马逊更新爬取过滤器关键字集合
     */
    public List<String> getUpdateFilters(String star) {

        List<String> list = new ArrayList<String>();
        String[] starsArray = star.split("-");

        for (int i = 0, len = starsArray.length; i < len; i++) {
            if ("1".equals(starsArray[i])) {
                String filter;
                if (i == 0) {
                    filter = "five_star";
                } else if (i == 1) {
                    filter = "four_star";
                } else if (i == 2) {
                    filter = "three_star";
                } else if (i == 3) {
                    filter = "two_star";
                } else {
                    filter = "one_star";
                }
                list.add(filter);
            }
        }
        return list;
    }

    public boolean isExist(String siteCode, String rootAsin) {
        return mAsinDao.findByAsin(siteCode, rootAsin) != null;
    }

    public void add(Asin asin) {
        if (isExist(asin.siteCode, asin.rootAsin)) {
            update(asin);
        } else {
            mAsinDao.add(asin);
        }
    }

}
