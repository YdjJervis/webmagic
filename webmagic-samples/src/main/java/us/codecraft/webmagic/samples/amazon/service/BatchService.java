package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.BatchDao;
import us.codecraft.webmagic.samples.amazon.pojo.*;
import us.codecraft.webmagic.samples.amazon.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户 业务
 * @date 2016/10/11
 */
@Service
public class BatchService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private BatchDao mBatchDao;

    @Autowired
    private AsinService mAsinService;

    @Autowired
    private BatchAsinService mBatchAsinService;

    @Autowired
    private UrlService mUrlService;

    @Autowired
    private ReviewMonitorService mReviewMonitorService;

    @Autowired
    private BatchReviewService mBatchReviewService;

    @Autowired
    private CustomerAsinService mCustomerAsinService;

    /**
     * 根据ASIN列表生成批次单号，批次详单，和转换成ASIN表
     * 记录
     *
     * @param customerCode 客户码
     * @param asinList     ASIN 列表
     */
    public Batch addBatch(String customerCode, List<Asin> asinList) {
        mLogger.info("开始处理客户的订单：customerCode = " + customerCode);

        if (CollectionUtils.isEmpty(asinList)) {
            mLogger.warn("asin list is null");
            return null;
        }

        Batch batch = generate(customerCode, 0);

        /* 把已经爬取过的和未爬取过的ASIN分开 */
        List<Asin> crawledList = new ArrayList<Asin>();
        List<Asin> newList = new ArrayList<Asin>();

        for (Asin asin : asinList) {
            if (mAsinService.isExist(asin.siteCode, asin.asin)) {
                crawledList.add(asin);
            } else {
                newList.add(asin);
            }
        }
        mLogger.info("ASIN列表大小：新的 " + newList.size() + " 已经爬取的 " + crawledList.size());
        /* 生成批次单详情列表，已经爬取过的和未爬取的做不同的处理 */
        List<BatchAsin> baList = new ArrayList<BatchAsin>();

        mLogger.info("对已经爬取过的列表插入到批次单明细前面...");
        float progress = 0;
        for (Asin asin : crawledList) {

            Asin byAsin = mAsinService.findByAsin(asin.siteCode, asin.asin);

            BatchAsin batchAsin = initBatchAsin(batch, asin);
            batchAsin.crawled = 1;

            /* 如果下架了，批次表进度直接改为100% */
            if (byAsin.onSale == 0) {
                batchAsin.progress = 1;
            } else {
                batchAsin.progress = byAsin.progress;
            }
            /* 已经在爬取的ASIN的进度 < 1就标记为全量爬取，否则标记为更新爬取 */
            batchAsin.type = batchAsin.progress < 1 ? 0 : 1;
            batchAsin.rootAsin = byAsin.rootAsin;
            batchAsin.extra = byAsin.extra;
            baList.add(batchAsin);

            progress += batchAsin.progress;
        }

        mLogger.info("新的批次单明细插入到列表后面...");
        for (Asin asin : newList) {

            BatchAsin ba = initBatchAsin(batch, asin);
            ba.rootAsin = asin.asin;
            baList.add(ba);
        }

        mLogger.info("批次单入库：" + batch);
        batch.progress = progress / (crawledList.size() + newList.size());
        add(batch);

        mLogger.info("批次详单入库：" + baList);
        mBatchAsinService.addAll(baList);

        /*
         * 添加ASIN批次到ASIN表中。
         * 步骤1，如果一部分不存在ASIN表中，则直接把列表转成ASIN表中的记录；
         */
        //步骤1
        List<Asin> parsedAsinList = new ArrayList<Asin>();
        mLogger.info("把新的ASIN批次转换成ASIN表记录：" + newList);
        for (Asin asin : newList) {
            Asin parsedAsin = new Asin();
            parsedAsin.asin = asin.asin;
            parsedAsinList.add(asin);
        }
        mAsinService.addAll(parsedAsinList);

        return batch;
    }

    public void add(Batch batch) {
        mBatchDao.add(batch);
    }

    public Batch addMonitor(String customerCode, List<Review> reviewList) {
        mLogger.info("开始处理Review监控：customerCode = " + customerCode);
        if (reviewList == null) {
            mLogger.warn("review monitor list is null");
            return null;
        }

        Batch batch = generate(customerCode, 1);

        List<Review> monitoringList = new ArrayList<Review>();
        List<Review> newList = new ArrayList<Review>();

        /* 把已经在监控中的和没有监控中的分开 */
        for (Review review : reviewList) {
            if (mReviewMonitorService.isExist(review.reviewId)) {
                monitoringList.add(review);
            } else {
                newList.add(review);
            }
        }

        mLogger.info("监控的Review列表大小：新的 " + newList.size() + " 已经在监控的 " + monitoringList.size());
        /* 生成批次单详情列表，已经爬取过的和未爬取的做不同的处理 */
        List<BatchReview> brList = new ArrayList<BatchReview>();

        /* 已爬取的 转换成批次详单*/
        for (Review review : monitoringList) {
            BatchReview batchReview = initBatchReview(batch, review);
            batchReview.crawled = 1;
            brList.add(batchReview);
        }

        /* 未爬取的 转换成批次详单*/
        for (Review review : newList) {
            BatchReview batchReview = initBatchReview(batch, review);
            brList.add(batchReview);
        }

        mBatchReviewService.addAll(brList);
        add(batch);

        /* 把未爬取的 和 已经爬取的 都转换成监控列表入库 */
        List<ReviewMonitor> reviewMonitorList = new ArrayList<ReviewMonitor>();
        for (Review review : newList) {
            ReviewMonitor reviewMonitor = new ReviewMonitor(review.reviewId);
            reviewMonitor.siteCode = review.siteCode;
            reviewMonitor.smrReviewId = review.reviewId;
            reviewMonitor.smrPriority = review.priority;
            reviewMonitorList.add(reviewMonitor);
        }
        mReviewMonitorService.addAll(reviewMonitorList);

        for (Review review : monitoringList) {

            ReviewMonitor monitor = mReviewMonitorService.findByReviewId(review.reviewId);
            if (review.priority > monitor.smrPriority) {
                monitor.smrPriority = review.priority;
                if (monitor.smrParsed == 1) {
                    mUrlService.updateMonitorPriority(monitor.smrReviewId, monitor.smrPriority);
                }
                mReviewMonitorService.update(monitor);
            }
        }

        return batch;
    }

    private BatchReview initBatchReview(Batch batch, Review review) {
        BatchReview batchReview = new BatchReview();
        batchReview.siteCode = review.siteCode;
        batchReview.batchNumber = batch.number;
        batchReview.reviewID = review.reviewId;
        batchReview.extra = review.extra;
        return batchReview;
    }

    private BatchAsin initBatchAsin(Batch batch, Asin asin) {
        BatchAsin ba = new BatchAsin();
        ba.batchNumber = batch.number;
        ba.siteCode = asin.siteCode;
        ba.asin = asin.asin;
        ba.rootAsin = asin.rootAsin;
        return ba;
    }

    /**
     * @param type 0-全量爬取；1-监听爬取
     */
    private Batch generate(String customerCode, int type) {

        Batch batch = new Batch();
        /* result like "EC20161110164710555" */
        batch.number = "EC" + DateUtils.format(new Date(), DateUtils.FORMAT_BATCH);
        batch.customerCode = customerCode;
        batch.type = type;

        mLogger.info("客户 " + customerCode + " 开始生成的批次号为 " + batch.number);
        return batch;
    }

    public Batch findByBatchNumber(String batchNumber) {
        return mBatchDao.findByBatchNumber(batchNumber);
    }

    public void update(Batch batch) {
        mBatchDao.update(batch);
    }

    public List<Batch> findByStatus(int status) {
        List<Batch> list = mBatchDao.findByStatus(status);
        for (Batch batch : list) {
            batch.status = 1;
            update(batch);
        }
        return list;
    }

}
