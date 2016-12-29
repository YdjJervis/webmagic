package us.codecraft.webmagic.samples.amazon.processor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Review;
import us.codecraft.webmagic.samples.amazon.pojo.StarReviewMap;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.pojo.batch.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchAsin;
import us.codecraft.webmagic.samples.amazon.pojo.relation.AsinRootAsin;
import us.codecraft.webmagic.samples.amazon.service.AsinService;
import us.codecraft.webmagic.samples.amazon.service.PushQueueService;
import us.codecraft.webmagic.samples.amazon.service.batch.BatchAsinService;
import us.codecraft.webmagic.samples.amazon.service.relation.AsinRootAsinService;
import us.codecraft.webmagic.samples.base.util.UrlUtils;
import us.codecraft.webmagic.selector.Selectable;

import java.util.*;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论更新爬取业务
 * @date 2016/10/14 15:24
 */
@Service
public class ReviewUpdateProcessor extends ReviewProcessor {

    @Autowired
    private AsinService mAsinService;
    @Autowired
    private AsinRootAsinService mAsinRootAsinService;
    @Autowired
    private BatchAsinService mBatchAsinService;
    @Autowired
    private PushQueueService mPushQueueService;

    @Override
    protected void dealReview(Page page) {

        Batch batch = mBatchService.findByBatchNumber(getUrl(page).batchNum);
        /* 更改开始爬取的时间 */
        Date currentTime = new Date();
        if (batch.startTime == null) {
            batch.startTime = currentTime;
        }

        String filter = UrlUtils.getValue(page.getUrl().get(), "filterByStar");

        List<Selectable> reviewNodeList = extractReviewNodeList(page);

        String asin = extractAsin(page);
        String siteCode = extractSite(page).code;

        sLogger.info("解析 " + siteCode + " 站点下ASIN码为 " + asin + " 的评论信息,当前URL=" + page.getUrl());

        AsinRootAsin asinRootAsin = mAsinRootAsinService.findByAsin(asin, siteCode);
        Asin dbAsin = mAsinService.findByAsin(siteCode, asinRootAsin.rootAsin);
        List<StarReviewMap> starReviewMapList = new Gson().fromJson(dbAsin.extra, new TypeToken<List<StarReviewMap>>() {
        }.getType());
        sLogger.info("全量爬取时候的星级对应的最后评论：");
        sLogger.info(dbAsin.extra);

        /* 把最后一次爬取的所有星级和对应的最后一条评论的ID加入Set集合 */
        Set<String> lastReviewSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(starReviewMapList)) {
            for (StarReviewMap map : starReviewMapList) {
                lastReviewSet.add(map.reviewID);
            }
        }

        /* 是否需要爬取下一页，默认是需要的 */
        boolean needCrawlNextPage = true;

        List<Review> reviewList = new ArrayList<Review>();
        for (Selectable reviewNode : reviewNodeList) {

            Review review = extractReviewItem(siteCode, reviewNode);
            if (lastReviewSet.contains(review.reviewId)) {
                needCrawlNextPage = false;
                break;
            }
            reviewList.add(review);
        }
        mReviewService.addAll(reviewList);

        /* 判断能否生成下一页URL并生成 */
        if (needCrawlNextPage) {
            /* 提取页码，若为空，就设置成 1 */
            String pageNum = UrlUtils.getValue(page.getUrl().get(), "pageNumber");
            pageNum = StringUtils.isEmpty(pageNum) ? "1" : pageNum;

            int currentPage = Integer.valueOf(pageNum);
            int totalPage = extractTotalPage(page);

            if (currentPage < totalPage) {

                /* 对URL的页码加 1 */
                String nextPageUrl = UrlUtils.setValue(page.getUrl().get(), "pageNumber", String.valueOf(currentPage + 1));

                /* 把新的Url放进爬取队列 */
                Url url = new Url();
                url.asin = asin;
                url.parentUrl = page.getUrl().get();
                url.url = nextPageUrl;
                url.urlMD5 = UrlUtils.md5(url.batchNum + nextPageUrl);
                url.siteCode = siteCode;
                url.type = R.CrawlType.REVIEW_UPDATE;

                mUrlService.add(url);
            } else {
                needCrawlNextPage = false;
            }
        }

        if (!needCrawlNextPage) {
            /* 不需要继续翻页，删除该星级对应的Url */
            List<Url> updateCrawlList = mUrlService.find(getUrl(page).batchNum, siteCode, asin, R.CrawlType.REVIEW_UPDATE);
            List<Url> deleteList = new ArrayList<>();

            /* 找出该过滤器的所有URL */
            for (Url url : updateCrawlList) {
                if (url.url.contains(filter)) {
                    deleteList.add(url);
                }
            }
            sLogger.info("需要删除的URL数量：" + deleteList.size() + " 该批次该站点该ASIN对应的URL总量：" + updateCrawlList.size());
            if (updateCrawlList.removeAll(deleteList) && updateCrawlList.size() == 0) {
                BatchAsin dbBatchAsin = mBatchAsinService.findAllByAsin(getUrl(page).batchNum, siteCode, asin);
                dbBatchAsin.status = 6;
                dbBatchAsin.progress = 1;
                mBatchAsinService.update(dbBatchAsin);

                /* 更新Asin大字段(星级：评论ID)数据 */
                Asin asinObj = mAsinService.updateExtra(dbAsin);

                /* 改变大字段的值 */
                dbBatchAsin.extra = mUrlService.getBatchAsinExtra(dbBatchAsin, asinObj.rootAsin);
                dbBatchAsin.finishTime = currentTime;

                /* 标记是否与上次相同 */
                if (!dbAsin.extra.equals(asinObj.extra)) {
                    dbBatchAsin.isChanged = 1;
                }

                mBatchAsinService.update(dbBatchAsin);

            }

            /* 添加到历史表 */
            mUrlHistoryService.addAll(deleteList);
            /* 删除爬取表 */
            for (Url url : deleteList) {
                mUrlService.deleteByUrlMd5(url.urlMD5);
            }

        }

        batch.progress = mBatchAsinService.findAverageProgress(getUrl(page).batchNum);

        /* 如果这个批次没有URL了，那就把该批次改成爬取完成状态 */
        if (mUrlService.findByBatchNum(getUrl(page).batchNum).size() == 0) {
            batch.status = 2;
            batch.finishTime = currentTime;

            /* 更新爬取完毕，放进推送队列 */
            mPushQueueService.add(batch.number);
        }
        mBatchService.update(batch);
    }

    @Override
    public void execute() {
        sLogger.info("开始执行更新爬取...");
        List<Url> urlList = mUrlService.find(R.CrawlType.REVIEW_UPDATE);

        BatchAsin batchAsin = null;
        Date currentTime = new Date();
        for (Url url : urlList) {
            batchAsin = mBatchAsinService.findAllByAsin(url.batchNum, url.siteCode, url.asin);
            if (batchAsin.startTime == null) {
                batchAsin.startTime = currentTime;
                mBatchAsinService.update(batchAsin);
            }
        }

        startToCrawl(urlList);
    }

}