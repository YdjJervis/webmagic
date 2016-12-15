package com.eccang.cxf;

import com.eccang.R;
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
import us.codecraft.webmagic.samples.amazon.util.DateUtils;

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
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = "Review列表为空";
            return baseRspParam.toJson();
        }

        ReviewRsp reviewRsp = new ReviewRsp();
        reviewRsp.cutomerCode = reviewReq.cutomerCode;
        reviewRsp.status = baseRspParam.status;
        reviewRsp.msg = baseRspParam.msg;

        try {
            int crawledNum = 0;
            /* 把客户和Review的关系保存起来 */
            List<CustomerReview> customerReviewList = new ArrayList<CustomerReview>();
            for (ReviewReq.Review review : reviewReq.data) {
                CustomerReview customerReview = new CustomerReview();
                customerReview.customerCode = reviewReq.cutomerCode;
                customerReview.siteCode = review.siteCode;
                customerReview.asin = review.asin;
                customerReview.reviewId = review.reviewId;
                customerReview.priority = review.priority;
                customerReview.frequency = review.frequency;
                if (mCustomerReviewService.isExist(reviewReq.cutomerCode, review.reviewId)) {
                    crawledNum++;
                }
                customerReviewList.add(customerReview);
            }
            mCustomerReviewService.addAll(customerReviewList);

            reviewRsp.data.totalCount = customerReviewList.size();
            reviewRsp.data.newCount = customerReviewList.size() - crawledNum;
            reviewRsp.data.oldCount = crawledNum;
        } catch (Exception e) {
            sLogger.error(e);
            reviewRsp.status = R.HttpStatus.SERVER_EXCEPTION;
            reviewRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
        }

        return reviewRsp.toJson();
    }

    @Override
    public String getReviews(String asinJson) {

        BaseRspParam baseRspParam = auth(asinJson);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        ReviewQueryReq reviewQueryReq = new Gson().fromJson(asinJson, ReviewQueryReq.class);
        if (reviewQueryReq.data == null) {
            baseRspParam.status = 413;
            baseRspParam.msg = "Asin数据为空";
            return baseRspParam.toJson();
        }

        /* 初始化返回头信息 */
        ReviewQueryRsp reviewQueryRsp = new ReviewQueryRsp();
        reviewQueryRsp.cutomerCode = baseRspParam.cutomerCode;
        reviewQueryRsp.status = baseRspParam.status;
        reviewQueryRsp.msg = baseRspParam.msg;

        try {
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
                resultReview.time = DateUtils.format(review.dealTime);
                resultReview.personID = review.personId;
                resultReview.reviewID = review.reviewId;
                resultReview.buyStatus = review.buyStatus;
                resultReview.star = review.star;
                resultReview.title = review.title;
                resultReview.content = review.content;
                reviewQueryRsp.data.add(resultReview);
            }
        } catch (NumberFormatException e) {
            sLogger.error(e);
            reviewQueryRsp.status = R.HttpStatus.SERVER_EXCEPTION;
            reviewQueryRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
        }

        return reviewQueryRsp.toJson();
    }

    /**
     * 设置监听review的优先级
     */
    @Override
    public String setPriority(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CustomerReviewUpdateReq customerReviewUpdateReq = new Gson().fromJson(json, CustomerReviewUpdateReq.class);
        if (CollectionUtils.isEmpty(customerReviewUpdateReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = "数据列表为空";
            return baseRspParam.toJson();
        }

        CustomerReviewUpdateRsp customerReviewUpdateRsp = new CustomerReviewUpdateRsp();
        customerReviewUpdateRsp.cutomerCode = customerReviewUpdateReq.cutomerCode;
        customerReviewUpdateRsp.status = baseRspParam.status;
        customerReviewUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CustomerReviewUpdateReq.CustomerReview customerReview : customerReviewUpdateReq.data) {
                CustomerReview data = mCustomerReviewService.findCustomerReview(baseRspParam.cutomerCode, customerReview.reviewId);
                if (customerReview.priority == data.priority) {
                    customerReviewUpdateRsp.data.noChange++;
                } else {
                    data.priority = customerReview.priority;
                    mCustomerReviewService.update(data);
                    customerReviewUpdateRsp.data.changed++;
                }
            }
        } catch (Exception e) {
            sLogger.error(e);
            customerReviewUpdateRsp.status = R.HttpStatus.SERVER_EXCEPTION;
            customerReviewUpdateRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
        }
        return customerReviewUpdateRsp.toJson();
    }

    /**
     * 设置监听review是否开启或关闭
     */
    @Override
    public String setReviewMonitor(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CustomerReviewUpdateReq customerReviewUpdateReq = new Gson().fromJson(json, CustomerReviewUpdateReq.class);
        if (CollectionUtils.isEmpty(customerReviewUpdateReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = "数据列表为空";
            return baseRspParam.toJson();
        }

        CustomerReviewUpdateRsp customerReviewUpdateRsp = new CustomerReviewUpdateRsp();
        customerReviewUpdateRsp.cutomerCode = customerReviewUpdateReq.cutomerCode;
        customerReviewUpdateRsp.status = baseRspParam.status;
        customerReviewUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CustomerReviewUpdateReq.CustomerReview customerReview : customerReviewUpdateReq.data) {
                CustomerReview data = mCustomerReviewService.findCustomerReview(baseRspParam.cutomerCode, customerReview.reviewId);
                if (customerReview.status == data.status) {
                    customerReviewUpdateRsp.data.noChange++;
                } else {
                    data.status = customerReview.status;
                    mCustomerReviewService.update(data);
                    customerReviewUpdateRsp.data.changed++;
                }
            }
        } catch (Exception e) {
            sLogger.error(e);
            customerReviewUpdateRsp.status = R.HttpStatus.SERVER_EXCEPTION;
            customerReviewUpdateRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
        }
        return customerReviewUpdateRsp.toJson();
    }

    /**
     * 更新监听Review的监听频率（h/次）
     */
    @Override
    public String setFrequency(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CustomerReviewUpdateReq customerReviewUpdateReq = new Gson().fromJson(json, CustomerReviewUpdateReq.class);
        if (CollectionUtils.isEmpty(customerReviewUpdateReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = "数据列表为空";
            return baseRspParam.toJson();
        }

        CustomerReviewUpdateRsp customerReviewUpdateRsp = new CustomerReviewUpdateRsp();
        customerReviewUpdateRsp.cutomerCode = customerReviewUpdateReq.cutomerCode;
        customerReviewUpdateRsp.status = baseRspParam.status;
        customerReviewUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CustomerReviewUpdateReq.CustomerReview customerReview : customerReviewUpdateReq.data) {
                CustomerReview data = mCustomerReviewService.findCustomerReview(baseRspParam.cutomerCode, customerReview.reviewId);
                if (customerReview.frequency == data.frequency) {
                    customerReviewUpdateRsp.data.noChange++;
                } else {
                    data.frequency = customerReview.frequency;
                    mCustomerReviewService.update(data);
                    customerReviewUpdateRsp.data.changed++;
                }
            }
        } catch (Exception e) {
            sLogger.error(e);
            customerReviewUpdateRsp.status = R.HttpStatus.SERVER_EXCEPTION;
            customerReviewUpdateRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
        }
        return customerReviewUpdateRsp.toJson();
    }

    /**
     * 更新更新监听Review的状态
     */
    public String updateCustomerReview(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CustomerReviewUpdateReq customerReviewUpdateReq = new Gson().fromJson(json, CustomerReviewUpdateReq.class);
        if (CollectionUtils.isEmpty(customerReviewUpdateReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = "数据列表为空";
            return baseRspParam.toJson();
        }

        CustomerReviewUpdateRsp customerReviewUpdateRsp = new CustomerReviewUpdateRsp();
        customerReviewUpdateRsp.cutomerCode = customerReviewUpdateReq.cutomerCode;
        customerReviewUpdateRsp.status = baseRspParam.status;
        customerReviewUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CustomerReviewUpdateReq.CustomerReview customerReview : customerReviewUpdateReq.data) {
                CustomerReview data = mCustomerReviewService.findCustomerReview(baseRspParam.cutomerCode, customerReview.reviewId);
                if (customerReview.frequency == data.frequency &&
                        customerReview.priority == data.priority &&
                        customerReview.status == data.status) {
                    customerReviewUpdateRsp.data.noChange++;
                } else {
                    data.priority = customerReview.priority;
                    data.frequency = customerReview.frequency;
                    data.status = customerReview.status;
                    mCustomerReviewService.update(data);
                    customerReviewUpdateRsp.data.changed++;
                }
            }
        } catch (Exception e) {
            sLogger.error(e);
            customerReviewUpdateRsp.status = R.HttpStatus.SERVER_EXCEPTION;
            customerReviewUpdateRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
        }
        return customerReviewUpdateRsp.toJson();
    }

}