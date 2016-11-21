package com.eccang.cxf;

import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.ReviewReq;
import com.eccang.pojo.ReviewRsp;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.BatchReview;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.service.BatchReviewService;
import us.codecraft.webmagic.samples.amazon.service.BatchService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 添加Review批次的WebService调用实现
 * @date 2016/11/17 11:51
 */
@WebService
public class ReviewWSImpl extends AbstractSpiderWS implements ReviewWS {

    @Autowired
    private BatchService mBatchService;

    @Autowired
    private BatchReviewService mBatchReviewService;

    @WebMethod
    public String addToMonitor(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        ReviewReq reviewReq = new Gson().fromJson(json, ReviewReq.class);
        if (CollectionUtils.isEmpty(reviewReq.data)) {
            baseRspParam.status = 413;
            baseRspParam.msg = "Review列表为空";
            return baseRspParam.toJson();
        }

        ReviewRsp reviewRsp = new ReviewRsp();
        reviewRsp.cutomerCode = reviewReq.cutomerCode;
        reviewRsp.status = baseRspParam.status;
        reviewRsp.msg = baseRspParam.msg;

        List<Review> parsedReviewList = new ArrayList<Review>();

        for (ReviewReq.Review review : reviewReq.data) {
            Review parsedReview = new Review();
            parsedReview.sarReviewId = review.reviewID;
            parsedReview.basCode = review.siteCode;
            parsedReview.priority = review.priority;
            parsedReviewList.add(parsedReview);
        }

        Batch batch = mBatchService.addMonitor(reviewRsp.cutomerCode, parsedReviewList);

        List<BatchReview> batchReviewList = mBatchReviewService.findAllByBatchNum(batch.number);

        /* 统计新添加的ASIN的个数 */
        int newCount = 0;
        for (BatchReview batchReview : batchReviewList) {
            if(batchReview.crawled == 0){
                newCount++;
            }
        }

        reviewRsp.data.number = batch.number;
        reviewRsp.data.totalCount = batchReviewList.size();
        reviewRsp.data.newCount = newCount;
        reviewRsp.data.oldCount = batchReviewList.size() - newCount;

        return reviewRsp.toJson();
    }

}