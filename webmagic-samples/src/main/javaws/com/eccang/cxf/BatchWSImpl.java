package com.eccang.cxf;

import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.BatchAsinRsp;
import com.eccang.pojo.BatchReq;
import com.eccang.pojo.BatchReviewRsp;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.BatchAsin;
import us.codecraft.webmagic.samples.amazon.pojo.BatchReview;
import us.codecraft.webmagic.samples.amazon.service.BatchAsinService;
import us.codecraft.webmagic.samples.amazon.service.BatchReviewService;
import us.codecraft.webmagic.samples.amazon.service.BatchService;
import us.codecraft.webmagic.samples.amazon.util.DateUtils;

import javax.jws.WebService;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 添加ASIN批次的WebService调用实现
 * @date 2016/11/17 11:51
 */
@WebService
public class BatchWSImpl extends AbstractSpiderWS implements BatchWS {

    @Autowired
    private BatchService mBatchService;

    @Autowired
    private BatchAsinService mBatchAsinService;

    @Autowired
    private BatchReviewService mBatchReviewService;

    @Override
    public String getBatchInfo(String json) {

        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        BatchReq batchReq = new Gson().fromJson(json, BatchReq.class);

        Batch batch = mBatchService.findByBatchNumber(batchReq.data.number);

        if (batch == null) {
            baseRspParam.status = 413;
            baseRspParam.msg = "没有这个批次单号:" + batchReq.data.number;
            return baseRspParam.toJson();
        }

        if (batch.type == 0 || batch.type == 2) {
            BatchAsinRsp batchAsinRsp = new BatchAsinRsp();
            batchAsinRsp.cutomerCode = baseRspParam.cutomerCode;
            batchAsinRsp.status = baseRspParam.status;
            batchAsinRsp.msg = baseRspParam.msg;

            batchAsinRsp.data.number = batchReq.data.number;
            batchAsinRsp.data.createTime = DateUtils.format(batch.createTime);
            batchAsinRsp.data.type = batch.type;
            batchAsinRsp.data.times = batch.times;
            batchAsinRsp.data.startTime = DateUtils.format(batch.startTime);
            batchAsinRsp.data.finishTime = DateUtils.format(batch.finishTime);
            batchAsinRsp.data.progress = batch.progress;
            batchAsinRsp.data.updateTime = DateUtils.format(batch.updateTime);

            List<BatchAsin> batchAsinList = mBatchAsinService.findAllByBatchNum(batchReq.data.number);
            for (BatchAsin batchAsin : batchAsinList) {
                BatchAsinRsp.Asin asin = batchAsinRsp.new Asin();
                asin.siteCode = batchAsin.siteCode;
                asin.asin = batchAsin.asin;
                asin.rootAsin = batchAsin.rootAsin;
                asin.crawled = batchAsin.crawled;
                asin.progress = batchAsin.progress;
                asin.startTime = DateUtils.format(batchAsin.startTime);
                asin.finishTime = DateUtils.format(batchAsin.finishTime);
                asin.updateTime = DateUtils.format(batchAsin.updateTime);
                batchAsinRsp.data.details.add(asin);
            }

            return batchAsinRsp.toJson();
        } else if (batch.type == 1) {
            BatchReviewRsp batchReviewRsp = new BatchReviewRsp();
            batchReviewRsp.cutomerCode = baseRspParam.cutomerCode;
            batchReviewRsp.status = baseRspParam.status;
            batchReviewRsp.msg = baseRspParam.msg;

            batchReviewRsp.data.createTime = DateUtils.format(batch.createTime);
            batchReviewRsp.data.number = batchReq.data.number;
            batchReviewRsp.data.type = batch.type;
            batchReviewRsp.data.times = batch.times;
            batchReviewRsp.data.startTime = DateUtils.format(batch.startTime);
            batchReviewRsp.data.finishTime = DateUtils.format(batch.finishTime);
            batchReviewRsp.data.progress = batch.progress;
            batchReviewRsp.data.updateTime = DateUtils.format(batch.updateTime);

            List<BatchReview> batchReviewList = mBatchReviewService.findAllByBatchNum(batchReq.data.number);
            for (BatchReview batchReview : batchReviewList) {
                BatchReviewRsp.ReviewMonitor monitor = batchReviewRsp.new ReviewMonitor();
                monitor.siteCode = batchReview.siteCode;
                monitor.reviewID = batchReview.reviewID;
                monitor.updateTime = DateUtils.format(batchReview.updateTime);
                batchReviewRsp.data.details.add(monitor);
            }

            return batchReviewRsp.toJson();
        }
        return null;
    }
}