package com.eccang.cxf.batch;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.asin.BatchAsinRsp;
import com.eccang.pojo.batch.BatchReq;
import com.eccang.pojo.batch.BatchRsp;
import com.eccang.pojo.followsell.BatchFollowSellRsp;
import com.eccang.pojo.rank.BatchRankRsp;
import com.eccang.pojo.review.BatchReviewRsp;
import com.eccang.spider.amazon.pojo.batch.*;
import com.eccang.spider.amazon.pojo.relation.CustomerReview;
import com.eccang.spider.amazon.service.batch.*;
import com.eccang.spider.amazon.service.relation.CustomerReviewService;
import com.eccang.spider.amazon.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private BatchRankService mBatchRankService;

    @Autowired
    private BatchFollowSellService mBatchFollowSellService;

    Logger sLogger = Logger.getLogger(getClass());

    @Override
    public String getBatchInfo(String json) {

        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        BatchReq batchReq = parseRequestParam(json, baseRspParam, BatchReq.class);
        if (batchReq == null) {
            return baseRspParam.toJson();
        }

        /*校验batch对象是否为null*/
        if (batchReq.data == null) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_BATCH_NULL_ERROR;
            return baseRspParam.toJson();
        }

        /*校验batch对象中批次号number是否为空*/
        if (StringUtils.isEmpty(batchReq.data.number)) {
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
            batchAsinRsp.customerCode = baseRspParam.customerCode;
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
                BatchAsinRsp.Asin asin;
                for (BatchAsin batchAsin : batchAsinList) {
                    asin = batchAsinRsp.new Asin();
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
                serverException(batchAsinRsp, e);
            }

            return batchAsinRsp.toJson();
        } else if (batch.type == 1) {
            BatchReviewRsp batchReviewRsp = new BatchReviewRsp();
            batchReviewRsp.customerCode = baseRspParam.customerCode;
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
                    CustomerReview customerReview = mCustomerReviewService.findCustomerReview(baseRspParam.customerCode, batchReview.reviewID);
                    BatchReviewRsp.ReviewMonitor monitor = batchReviewRsp.new ReviewMonitor();
                    monitor.siteCode = batchReview.siteCode;
                    monitor.reviewID = batchReview.reviewID;
                    monitor.isChanged = batchReview.isChanged;
                    monitor.progress = batchReview.status == 2 ? 1 : 0;
                    monitor.asin = customerReview.asin;
                    monitor.updateTime = DateUtils.format(batchReview.updateTime);
                    batchReviewRsp.data.details.add(monitor);
                }
            } catch (Exception e) {
                serverException(batchReviewRsp, e);
            }

            return batchReviewRsp.toJson();
        } else if (batch.type == 3) {
            BatchFollowSellRsp batchFollowSellRsp = new BatchFollowSellRsp();
            batchFollowSellRsp.customerCode = baseRspParam.customerCode;
            batchFollowSellRsp.status = baseRspParam.status;
            batchFollowSellRsp.msg = baseRspParam.msg;

            try {
                batchFollowSellRsp.data.createTime = DateUtils.format(batch.createTime);
                batchFollowSellRsp.data.number = batchReq.data.number;
                batchFollowSellRsp.data.type = batch.type;
                batchFollowSellRsp.data.status = batch.status;
                batchFollowSellRsp.data.times = batch.times;
                batchFollowSellRsp.data.startTime = DateUtils.format(batch.startTime);
                batchFollowSellRsp.data.finishTime = DateUtils.format(batch.finishTime);
                batchFollowSellRsp.data.progress = batch.progress;
                batchFollowSellRsp.data.updateTime = DateUtils.format(batch.updateTime);

                List<BatchFollowSell> batchFollowSells = mBatchFollowSellService.findAllByBatchNum(batchReq.data.number);
                BatchFollowSellRsp.FollowSell followSell;
                for (BatchFollowSell batchFollowSell : batchFollowSells) {
                    followSell = batchFollowSellRsp.new FollowSell();
                    followSell.asin = batchFollowSell.asin;
                    followSell.siteCode = batchFollowSell.siteCode;
                    followSell.progress = batchFollowSell.status == 2 ? 1 : 0;
                    followSell.isChanged = batchFollowSell.isChanged;
                    followSell.priority = batchFollowSell.priority;
                    followSell.createTime = DateUtils.format(batchFollowSell.createTime);
                    followSell.updateTime = DateUtils.format(batchFollowSell.updateTime);
                    batchFollowSellRsp.data.details.add(followSell);
                }
            } catch (Exception e) {
                serverException(batchFollowSellRsp, e);
            }

            return batchFollowSellRsp.toJson();
        } else if (batch.type == 4) {
            BatchRankRsp batchRankRsp = new BatchRankRsp();
            batchRankRsp.customerCode = baseRspParam.customerCode;
            batchRankRsp.status = baseRspParam.status;
            batchRankRsp.msg = baseRspParam.msg;

            try {
                batchRankRsp.data.createTime = DateUtils.format(batch.createTime);
                batchRankRsp.data.number = batchReq.data.number;
                batchRankRsp.data.type = batch.type;
                batchRankRsp.data.status = batch.status;
                batchRankRsp.data.times = batch.times;
                batchRankRsp.data.startTime = DateUtils.format(batch.startTime);
                batchRankRsp.data.finishTime = DateUtils.format(batch.finishTime);
                batchRankRsp.data.progress = batch.progress;
                batchRankRsp.data.updateTime = DateUtils.format(batch.updateTime);

                BatchRankRsp.KeywordRank keywordRank;
                List<BatchRank> batchRanks = mBatchRankService.findByBatch(batchReq.data.number);
                for (BatchRank batchRank : batchRanks) {
                    keywordRank = batchRankRsp.new KeywordRank();
                    keywordRank.siteCode = batchRank.getSiteCode();
                    keywordRank.departmentCode = batchRank.getDepartmentCode();
                    keywordRank.keyword = batchRank.getKeyword();
                    keywordRank.asin = batchRank.getAsin();
                    keywordRank.progress = batchRank.getStatus() == 2 ? 1 : 0;
                    keywordRank.isChanged = batchRank.getIsChanged();
                    keywordRank.priority = batchRank.getPriority();
                    keywordRank.createTime = DateUtils.format(batchRank.getCreateTime());
                    keywordRank.updateTime = DateUtils.format(batchRank.getUpdateTime());
                    batchRankRsp.data.details.add(keywordRank);
                }
            } catch (Exception e) {
                serverException(batchRankRsp, e);
            }

            return batchRankRsp.toJson();
        }
        return null;
    }

    @Override
    public String getBatches(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        BatchRsp batchRsp = new BatchRsp();
        batchRsp.customerCode = baseRspParam.customerCode;
        batchRsp.status = baseRspParam.status;
        batchRsp.msg = baseRspParam.msg;

        try {
            /*通过客户码查询总单*/
            List<Batch> batchList = mBatchService.findByCustomer(baseRspParam.customerCode);
            if (CollectionUtils.isEmpty(batchList)) {
                return batchRsp.toJson();
            }

            BatchRsp.BatchInfo batchInfo;
            for (Batch batch : batchList) {
                batchInfo = batchRsp.new BatchInfo();
                batchInfo.number = batch.number;
                batchInfo.importType = batch.isImport;
                batchInfo.type = batch.type;
                batchInfo.status = batch.status;
                batchInfo.times = batch.times;
                batchInfo.startTime = DateUtils.format(batch.startTime);
                batchInfo.progress = batch.progress;
                batchInfo.finishTime = DateUtils.format(batch.finishTime);
                batchInfo.createTime = DateUtils.format(batch.createTime);
                batchInfo.updateTime = DateUtils.format(batch.updateTime);
                batchRsp.data.add(batchInfo);
            }
        } catch (Exception e) {
            serverException(batchRsp, e);
        }

        return batchRsp.toJson();
    }
}