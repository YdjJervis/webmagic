package com.eccang.cxf;

import com.eccang.pojo.*;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.CustomerReview;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.service.CustomerReviewService;
import us.codecraft.webmagic.samples.amazon.service.ReviewService;

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
    private ReviewService mReviewService;

    @Autowired
    private CustomerReviewService mCustomerReviewService;

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

        int crawledNum = 0;
        List<CustomerReview> customerReviewList = new ArrayList<CustomerReview>();
        for (ReviewReq.Review review : reviewReq.data) {
            CustomerReview customerReview = new CustomerReview();
            customerReview.customerCode = reviewReq.cutomerCode;
            customerReview.siteCode = review.siteCode;
            customerReview.reviewId = review.reviewID;
            customerReview.priority = review.priority;
            customerReview.frequency = review.frequency;

            if (mCustomerReviewService.isExist(reviewReq.cutomerCode, review.reviewID)) {
                crawledNum++;
            }
        }

        mCustomerReviewService.addAll(customerReviewList);

        reviewRsp.data.totalCount = customerReviewList.size();
        reviewRsp.data.newCount = customerReviewList.size() - crawledNum;
        reviewRsp.data.oldCount = crawledNum;

        return reviewRsp.toJson();
    }

    @Override
    public String getReviews(String asinJson) {

        BaseRspParam baseRspParam = auth(asinJson);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        ReviewQueryReq reviewQueryReq = new Gson().fromJson(asinJson, ReviewQueryReq.class);

        /* 初始化返回头信息 */
        ReviewQueryRsp reviewQueryRsp = new ReviewQueryRsp();
        reviewQueryRsp.cutomerCode = baseRspParam.cutomerCode;
        reviewQueryRsp.status = baseRspParam.status;
        reviewQueryRsp.msg = baseRspParam.msg;

        /* 默认查询差评的 */
        if (StringUtils.isEmpty(reviewQueryReq.data.level)) {
            reviewQueryReq.data.level = "0-0-1-1-1";
        }
        /* 传的参数不包含三个类型的，就默认为全部的 */
        if (!Sets.newHashSet("yes", "no", "all").contains(reviewQueryReq.data.experience)) {
            reviewQueryReq.data.experience = "all";
        }

        if (reviewQueryReq.data.pageSize == 0 || reviewQueryReq.data.pageSize > 100) {
            reviewQueryReq.data.pageSize = 50;
        }

        String[] levels = StringUtils.reverse(reviewQueryReq.data.level).split("-");
        List<Review> allReviewList = new ArrayList<Review>();
        /* 先返回低星级的评论 */
        for (int i = 0; i < levels.length; i++) {

            if (Integer.valueOf(levels[i]) == 1) {
                Review queryReview = new Review();
                queryReview.personId = reviewQueryReq.data.personID;
                queryReview.star = 5 - i;
                List<Review> reviewList = mReviewService.findAll(queryReview);
                allReviewList.addAll(reviewList);
            }
        }

        /* 把查询的Review转换成需要返回的对象 */
        for (Review review : allReviewList) {
            ReviewQueryRsp.Review resultReview = reviewQueryRsp.new Review();
            resultReview.siteCode = review.siteCode;
            resultReview.time = review.dealTime;
            resultReview.personID = review.personId;
            resultReview.reviewID = review.reviewId;
            resultReview.buyStatus = review.buyStatus;
            resultReview.star = review.star;
            resultReview.title = review.title;
            resultReview.content = review.content;
            reviewQueryRsp.data.add(resultReview);
        }

        return reviewQueryRsp.toJson();
    }
}