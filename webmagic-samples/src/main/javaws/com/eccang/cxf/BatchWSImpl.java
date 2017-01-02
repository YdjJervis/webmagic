package com.eccang.cxf;

import com.eccang.R;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.BatchAsinRsp;
import com.eccang.pojo.BatchReq;
import com.eccang.pojo.BatchReviewRsp;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.pojo.batch.BatchReview;
import com.eccang.spider.amazon.pojo.relation.CustomerReview;
import com.eccang.spider.amazon.service.batch.BatchAsinService;
import com.eccang.spider.amazon.service.batch.BatchReviewService;
import com.eccang.spider.amazon.service.batch.BatchService;
import com.eccang.spider.amazon.service.relation.CustomerReviewService;
import com.eccang.spider.amazon.util.DateUtils;

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

    @Autowired
    private CustomerReviewService mCustomerReviewService;

    Logger sLogger = Logger.getLogger(getClass());

    @Override
    public String getBatchInfo(String json) {

        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        BatchReq batchReq;
        try {
            batchReq = new Gson().fromJson(json, BatchReq.class);
        } catch (Exception e) {
            sLogger.info(e);
            baseRspParam.status  = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        /*校验batch对象是否为null*/
        if(batchReq.data == null) {
            baseRspParam.status  = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_BATCH_NULL_ERROR;
            return baseRspParam.toJson();
        }

        /*校验batch对象中批次号number是否为空*/
        if(StringUtils.isEmpty(batchReq.data.number)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_BATCH_NUM_ERROR;
            return baseRspParam.toJson();
        }

        Batch batch = mBatchService.findByBatchNumber(batchReq.data.number);

        if (batch == null) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = "没有这个批次单号:" + batchReq.data.number;
            return baseRspParam.toJson();
        }

        if (batch.type == 0 || batch.type == 2) {
            BatchAsinRsp batchAsinRsp = new BatchAsinRsp();
            batchAsinRsp.cutomerCode = baseRspParam.cutomerCode;
            batchAsinRsp.status = baseRspParam.status;
            batchAsinRsp.msg = baseRspParam.msg;

            try {
                batchAsinRsp.data.number = batchReq.data.number;
                batchAsinRsp.data.createTime = DateUtils.format(batch.createTime);
                batchAsinRsp.data.type = batch.type;
                batchAsinRsp.data.status = batch.status;
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
                    asin.rootAsin = batchAsin.rootAsin == null ? "" : batchAsin.rootAsin;
                    asin.isChanged = batchAsin.isChanged;
                    asin.progress = batchAsin.progress;
                    asin.startTime = DateUtils.format(batchAsin.startTime);
                    asin.finishTime = DateUtils.format(batchAsin.finishTime);
                    asin.updateTime = DateUtils.format(batchAsin.updateTime);
                    batchAsinRsp.data.details.add(asin);
                }
            } catch (Exception e) {
                sLogger.error(e);
                batchAsinRsp.status = R.HttpStatus.SERVER_EXCEPTION;
                batchAsinRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
            }

            return batchAsinRsp.toJson();
        } else if (batch.type == 1) {
            BatchReviewRsp batchReviewRsp = new BatchReviewRsp();
            batchReviewRsp.cutomerCode = baseRspParam.cutomerCode;
            batchReviewRsp.status = baseRspParam.status;
            batchReviewRsp.msg = baseRspParam.msg;

            try {
                batchReviewRsp.data.createTime = DateUtils.format(batch.createTime);
                batchReviewRsp.data.number = batchReq.data.number;
                batchReviewRsp.data.type = batch.type;
                batchReviewRsp.data.status = batch.status;
                batchReviewRsp.data.times = batch.times;
                batchReviewRsp.data.startTime = DateUtils.format(batch.startTime);
                batchReviewRsp.data.finishTime = DateUtils.format(batch.finishTime);
                batchReviewRsp.data.progress = batch.progress;
                batchReviewRsp.data.updateTime = DateUtils.format(batch.updateTime);

                List<BatchReview> batchReviewList = mBatchReviewService.findAllByBatchNum(batchReq.data.number);
                for (BatchReview batchReview : batchReviewList) {
                    CustomerReview customerReview = mCustomerReviewService.findCustomerReview(baseRspParam.cutomerCode,batchReview.reviewID);
                    BatchReviewRsp.ReviewMonitor monitor = batchReviewRsp.new ReviewMonitor();
                    monitor.siteCode = batchReview.siteCode;
                    monitor.reviewID = batchReview.reviewID;
                    monitor.isChanged = batchReview.isChanged;
                    monitor.progress =  batchReview.status == 2 ? 1 : 0;
                    monitor.asin = customerReview.asin;
                    monitor.updateTime = DateUtils.format(batchReview.updateTime);
                    batchReviewRsp.data.details.add(monitor);
                }
            } catch (Exception e) {
                sLogger.error(e);
                batchReviewRsp.status = R.HttpStatus.SERVER_EXCEPTION;
                batchReviewRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
            }

            return batchReviewRsp.toJson();
        }
        return null;
    }
}