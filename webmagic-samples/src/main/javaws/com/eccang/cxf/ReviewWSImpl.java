package com.eccang.cxf;

import com.eccang.R;
import com.eccang.pojo.*;
import com.eccang.util.RegexUtil;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.relation.AsinRootAsin;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerAsin;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerReview;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Review;
import us.codecraft.webmagic.samples.amazon.service.crawl.ReviewService;
import us.codecraft.webmagic.samples.amazon.service.relation.AsinRootAsinService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerAsinService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerBusinessService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerReviewService;
import us.codecraft.webmagic.samples.amazon.util.DateUtils;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private CustomerAsinService mCustomerAsinService;

    @Autowired
    private CustomerReviewService mCustomerReviewService;

    @Autowired
    private AsinRootAsinService mAsinRootAsinService;

    @Autowired
    private CustomerBusinessService mCustomerBusinessService;

    public String addToMonitor(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        ReviewReq reviewReq;
        try {
            reviewReq = new Gson().fromJson(json, ReviewReq.class);
        } catch (Exception e) {
            sLogger.info(e);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        Map<String, String> checkResult = checkData(reviewReq.data);
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
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
        /*对应客户下，review监听业务的使用情况*/
        Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(reviewReq.cutomerCode, R.BusinessCode.MONITOR_SPIDER);
        reviewRsp.data.usableNum = result.get(R.BusinessInfo.USABLENUM);
        reviewRsp.data.hasUsedNum = result.get(R.BusinessInfo.HASUSEDNUM);

        return reviewRsp.toJson();
    }

    @Override
    public String getReviewsByAsin(String asinJson) {

        BaseRspParam baseRspParam = auth(asinJson);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        ReviewQueryReq reviewQueryReq;
        try {
            reviewQueryReq = new Gson().fromJson(asinJson, ReviewQueryReq.class);
        } catch (Exception e) {
            sLogger.info(e);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        if (reviewQueryReq.data == null) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR;
            return baseRspParam.toJson();
        }

        /*查询asin的rootAsin*/
        String siteCode = reviewQueryReq.data.siteCode;
        String asin = reviewQueryReq.data.asin;
        String level = reviewQueryReq.data.level;

        if (StringUtils.isEmpty(siteCode)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR;
            return baseRspParam.toJson();
        }

        if (StringUtils.isEmpty(asin)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ERROR;
            return baseRspParam.toJson();
        }

        if (StringUtils.isNotEmpty(level) && !RegexUtil.isStarRegex(level)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_STAR_ERROR;
            return baseRspParam.toJson();
        }

        /* 初始化返回头信息 */
        ReviewQueryRsp reviewQueryRsp = new ReviewQueryRsp();
        reviewQueryRsp.cutomerCode = baseRspParam.cutomerCode;
        reviewQueryRsp.status = baseRspParam.status;
        reviewQueryRsp.msg = baseRspParam.msg;

        try {

            int pageNum = reviewQueryReq.data.pageNum;
            int pageSize = reviewQueryReq.data.pageSize;

            /* 默认查询差评的 */
            if (StringUtils.isEmpty(reviewQueryReq.data.level)) {
                reviewQueryReq.data.level = "0-0-1-1-1";
            }
            /* 传的参数不包含三个类型的，就默认为全部的 */
            if (!Sets.newHashSet("yes", "no").contains(reviewQueryReq.data.experience)) {
                reviewQueryReq.data.experience = null;
            }

            if (pageSize == 0 || pageSize > 100) {
                pageSize = 50;
            }

            if (pageNum == 0) {
                pageNum = 1;
            }

            CustomerAsin customerAsin = new CustomerAsin(baseRspParam.cutomerCode, siteCode, asin);
            String rootAsin;
            customerAsin = mCustomerAsinService.find(customerAsin);
            if (customerAsin != null) {
                AsinRootAsin asinRootAsin = mAsinRootAsinService.findByAsin(asin, siteCode);

                rootAsin = asinRootAsin == null ? null : asinRootAsin.rootAsin;
            } else {
                reviewQueryRsp.msg = "客户下,asin不存在.";
                return reviewQueryRsp.toJson();
            }

            if (StringUtils.isEmpty(rootAsin)) {
                reviewQueryRsp.msg = "此asin还没有开始爬取.";
                return reviewQueryRsp.toJson();
            }

            String[] levels = StringUtils.reverse(reviewQueryReq.data.level).split("-");

            /* 先返回低星级的评论 */
            Review queryReview = new Review();
            queryReview.rootAsin = rootAsin;
            queryReview.personId = reviewQueryReq.data.personID;
            queryReview.pageSize = pageSize;
            queryReview.pageNum = (pageNum - 1) * pageSize;
            queryReview.experience = reviewQueryReq.data.experience;

            for (int i = 0; i < levels.length; i++) {
                if (Integer.valueOf(levels[i]) == 1) {
                    queryReview.starList.add(i + 1);
                }
            }

            List<Review> reviewList = mReviewService.findAll(queryReview);

            reviewQueryRsp.pageNum = pageNum;
            reviewQueryRsp.pageSize = pageSize;
            reviewQueryRsp.pageTotal = mReviewService.findAllCount(queryReview);

            /* 把查询的Review转换成需要返回的对象 */
            for (Review review : reviewList) {
                ReviewQueryRsp.Review resultReview = reviewQueryRsp.new Review();
                initResultReview(resultReview, review, asin);
                reviewQueryRsp.data.add(resultReview);
            }
        } catch (NumberFormatException e) {
            sLogger.error(e);
            reviewQueryRsp.status = R.HttpStatus.SERVER_EXCEPTION;
            reviewQueryRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
        }

        return reviewQueryRsp.toJson();
    }

    @Override
    public String getReviewById(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        ReviewQueryReq reviewQueryReq;
        try {
            reviewQueryReq = new Gson().fromJson(json, ReviewQueryReq.class);
        } catch (Exception e) {
            sLogger.info(e);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        if (reviewQueryReq.data == null) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR;
            return baseRspParam.toJson();
        }

        String siteCode = reviewQueryReq.data.siteCode;
        String asin = reviewQueryReq.data.asin;

        if (StringUtils.isEmpty(siteCode)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR;
            return baseRspParam.toJson();
        }

        if (StringUtils.isEmpty(asin)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ERROR;
            return baseRspParam.toJson();
        }

        if (StringUtils.isEmpty(reviewQueryReq.data.reviewId)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_REVIEW_NULL_ERROR;
            return baseRspParam.toJson();
        }

        /* 初始化返回头信息 */
        ReviewQueryRsp reviewQueryRsp = new ReviewQueryRsp();
        reviewQueryRsp.cutomerCode = baseRspParam.cutomerCode;
        reviewQueryRsp.status = baseRspParam.status;
        reviewQueryRsp.msg = baseRspParam.msg;

        try {

            CustomerAsin customerAsin = new CustomerAsin(baseRspParam.cutomerCode, siteCode, asin);
            String rootAsin;
            customerAsin = mCustomerAsinService.find(customerAsin);
            if (customerAsin != null) {
                /*查询asin的rootAsin*/
                AsinRootAsin asinRootAsin = mAsinRootAsinService.findByAsin(asin, siteCode);
                rootAsin = asinRootAsin == null ? null : asinRootAsin.rootAsin;
            } else {
                reviewQueryRsp.msg = R.RequestMsg.PARAMETER_ASIN_EMPTY__ERROR;
                return reviewQueryRsp.toJson();
            }

            if (StringUtils.isEmpty(rootAsin)) {
                reviewQueryRsp.msg = R.RequestMsg.PARAMETER_ASIN_NOT_WORK__ERROR;
                return reviewQueryRsp.toJson();
            }

            List<Review> reviews = mReviewService.findByIdAndRootAsin(reviewQueryReq.data.reviewId, rootAsin);

            if (CollectionUtils.isEmpty(reviews)) {
                reviewQueryRsp.msg = R.RequestMsg.PARAMETER_REVIEW_NOT_EXIST__ERROR;
            }

            /* 把查询的Review转换成需要返回的对象 */
            for (Review review : reviews) {
                if (review != null) {
                    ReviewQueryRsp.Review resultReview = reviewQueryRsp.new Review();
                    initResultReview(resultReview, review, asin);
                    reviewQueryRsp.data.add(resultReview);
                }
            }

        } catch (NumberFormatException e) {
            sLogger.error(e);
            reviewQueryRsp.status = R.HttpStatus.SERVER_EXCEPTION;
            reviewQueryRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
        }

        return reviewQueryRsp.toJson();
    }

    /**
     * 初始化返回结果
     */
    private void initResultReview(ReviewQueryRsp.Review resultReview, Review review, String asin) {
        resultReview.asin = asin;
        resultReview.siteCode = review.siteCode;
        resultReview.time = DateUtils.format(review.dealTime);
        resultReview.personID = review.personId;
        resultReview.reviewID = review.reviewId;
        resultReview.buyStatus = StringUtils.isEmpty(review.buyStatus) ? "" : review.buyStatus;
        resultReview.person = review.person;
        resultReview.version = StringUtils.isEmpty(review.version) ?  "" : review.version;
        resultReview.star = review.star;
        resultReview.title = review.title;
        resultReview.content = review.content;
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

        CustomerReviewUpdateReq customerReviewUpdateReq;
        try {
            customerReviewUpdateReq = new Gson().fromJson(json, CustomerReviewUpdateReq.class);
        } catch (Exception e) {
            sLogger.info(e);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(customerReviewUpdateReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_NULL_ERROR;
            return baseRspParam.toJson();
        }

        for (CustomerReviewUpdateReq.CustomerReview customerReview : customerReviewUpdateReq.data) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;

            if (customerReview == null) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR;
                return baseRspParam.toJson();
            }

            if (!RegexUtil.isReviewIdQualified(customerReview.reviewId)) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_REVIEW_NULL_ERROR;
                return baseRspParam.toJson();
            }

            if (!RegexUtil.isPriorityQualified(customerReview.priority)) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_PRIORITY_ERROR;
                return baseRspParam.toJson();
            }
            baseRspParam.status = R.HttpStatus.SUCCESS;
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

        CustomerReviewUpdateReq customerReviewUpdateReq;
        try {
            customerReviewUpdateReq = new Gson().fromJson(json, CustomerReviewUpdateReq.class);
        } catch (Exception e) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(customerReviewUpdateReq.data)) {
            /*校验传入的请求是否为null*/
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_NULL_ERROR;
            return baseRspParam.toJson();
        }

        for (CustomerReviewUpdateReq.CustomerReview customerReview : customerReviewUpdateReq.data) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;

            if (customerReview == null) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR;
                return baseRspParam.toJson();
            }

            if (!RegexUtil.isReviewIdQualified(customerReview.reviewId)) {
                /*校验reviewId*/
                baseRspParam.msg = R.RequestMsg.PARAMETER_REVIEW_NULL_ERROR;
                return baseRspParam.toJson();
            }

            if (!RegexUtil.isMonitorStatusQualified(customerReview.crawl)) {
                /*校验状态*/
                baseRspParam.msg = R.RequestMsg.PARAMETER_STATUS_ERROR;
                return baseRspParam.toJson();
            }
            baseRspParam.status = R.HttpStatus.SUCCESS;
        }

        CustomerReviewUpdateRsp customerReviewUpdateRsp = new CustomerReviewUpdateRsp();
        customerReviewUpdateRsp.cutomerCode = customerReviewUpdateReq.cutomerCode;
        customerReviewUpdateRsp.status = baseRspParam.status;
        customerReviewUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CustomerReviewUpdateReq.CustomerReview customerReview : customerReviewUpdateReq.data) {
                CustomerReview data = mCustomerReviewService.findCustomerReview(baseRspParam.cutomerCode, customerReview.reviewId);

                if (data == null) {
                    customerReviewUpdateRsp.msg = R.RequestMsg.PARAMETER_REVIEW_EMPTY__ERROR;
                    return customerReviewUpdateRsp.toJson();
                }

                if (customerReview.crawl == data.crawl) {
                    customerReviewUpdateRsp.data.noChange++;
                } else {
                    data.crawl = customerReview.crawl;
                    mCustomerReviewService.update(data);
                    customerReviewUpdateRsp.data.changed++;
                }
            }
        } catch (Exception e) {
            sLogger.error(e);
            customerReviewUpdateRsp.status = R.HttpStatus.SERVER_EXCEPTION;
            customerReviewUpdateRsp.msg = R.RequestMsg.SERVER_EXCEPTION;
        }

        /*对应客户下，review监听业务的使用情况*/
        Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(baseRspParam.cutomerCode, R.BusinessCode.MONITOR_SPIDER);
        customerReviewUpdateRsp.data.usableNum = result.get(R.BusinessInfo.USABLENUM);
        customerReviewUpdateRsp.data.hasUsedNum = result.get(R.BusinessInfo.HASUSEDNUM);

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

        CustomerReviewUpdateReq customerReviewUpdateReq;
        try {
            customerReviewUpdateReq = new Gson().fromJson(json, CustomerReviewUpdateReq.class);
        } catch (Exception e) {
            sLogger.info(e);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(customerReviewUpdateReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_NULL_ERROR;
            return baseRspParam.toJson();
        }

        for (CustomerReviewUpdateReq.CustomerReview customerReview : customerReviewUpdateReq.data) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;

            if (customerReview == null) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR;
                return baseRspParam.toJson();
            }

            if (!RegexUtil.isReviewIdQualified(customerReview.reviewId)) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_REVIEW_NULL_ERROR;
                return baseRspParam.toJson();
            }

            if (!RegexUtil.isFrequencyQualified(customerReview.frequency)) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_REVIEW_FREQUENCY_ERROR;
                return baseRspParam.toJson();
            }
            baseRspParam.status = R.HttpStatus.SUCCESS;
        }

        CustomerReviewUpdateRsp customerReviewUpdateRsp = new CustomerReviewUpdateRsp();
        customerReviewUpdateRsp.cutomerCode = customerReviewUpdateReq.cutomerCode;
        customerReviewUpdateRsp.status = baseRspParam.status;
        customerReviewUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CustomerReviewUpdateReq.CustomerReview customerReview : customerReviewUpdateReq.data) {
                CustomerReview data = mCustomerReviewService.findCustomerReview(baseRspParam.cutomerCode, customerReview.reviewId);

                if(data == null) {
                    customerReviewUpdateRsp.msg = R.RequestMsg.PARAMETER_REVIEW_EMPTY__ERROR;
                    return customerReviewUpdateRsp.toJson();
                }

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
        /* 参数验证阶段 */
        boolean isParamQualified = true;
        if (CollectionUtils.isEmpty(customerReviewUpdateReq.data)) {
            isParamQualified = false;
        }
        for (CustomerReviewUpdateReq.CustomerReview customerReview : customerReviewUpdateReq.data) {
            if (!RegexUtil.isReviewIdQualified(customerReview.reviewId)
                    || !RegexUtil.isMonitorStatusQualified(customerReview.crawl)) {
                isParamQualified = false;
            }
        }
        if (!isParamQualified) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.LIST_PARAM_WRONG;
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
                        customerReview.crawl == data.crawl) {
                    customerReviewUpdateRsp.data.noChange++;
                } else {
                    data.priority = customerReview.priority;
                    data.frequency = customerReview.frequency;
                    data.crawl = customerReview.crawl;
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

    @Override
    public String getReviewsStatus(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CustomerReviewRsp customerReviewRsp = new CustomerReviewRsp();
        customerReviewRsp.cutomerCode = baseRspParam.cutomerCode;
        customerReviewRsp.status = baseRspParam.status;
        customerReviewRsp.msg = baseRspParam.msg;

        customerReviewRsp.data = new ArrayList<>();

        List<CustomerReview> customerReviewList = mCustomerReviewService.findCustomerReviewsByCustomerCode(baseRspParam.cutomerCode);
        CustomerReviewRsp.Review review;
        for (CustomerReview customerReview : customerReviewList) {
            review = customerReviewRsp.new Review();
            review.asin = customerReview.asin;
            review.reviewId = customerReview.reviewId;
            review.siteCode = customerReview.siteCode;
            review.crawl = customerReview.crawl;
            review.priority = customerReview.priority;
            review.frequency = customerReview.frequency;

            review.onSell = customerReview.onSell;
            review.crawlTime = customerReview.times;
            review.finishTime = DateUtils.format(customerReview.finishTime);
            review.createTime = DateUtils.format(customerReview.createTime);
            review.updateTime = DateUtils.format(customerReview.updateTime);
            customerReviewRsp.data.add(review);

        }

        return customerReviewRsp.toJson();
    }

    /**
     * 校验数据
     */
    private Map<String, String> checkData(List<ReviewReq.Review> reviews) {
        Map<String, String> checkResult = new HashMap<>();

        checkResult.put(IS_SUCCESS, "0");
        if (CollectionUtils.isEmpty(reviews)) {
            /*校验请求数据列表是否为空*/
            checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_NULL_ERROR);
            return checkResult;
        }

        for (ReviewReq.Review review : reviews) {

            if (review == null) {
                /*校验数据对象是否为空*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR);
                return checkResult;
            }

            if (StringUtils.isEmpty(review.asin)) {
                /*校验review是否有空的asin码*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_REVIEW_ASIN_ERROR);
                return checkResult;
            }

            if (!RegexUtil.isSiteCodeQualified(review.siteCode)) {
                /*校验review中站点码是否正确（为空或不在存在）*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR);
                return checkResult;
            }

            if (!RegexUtil.isFrequencyQualified(review.frequency)) {
                /*校验爬取频率是否在取值范围内*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_REVIEW_FREQUENCY_ERROR);
                return checkResult;
            }

            if (!RegexUtil.isReviewIdQualified(review.reviewId)) {
                /*校验reviewId是否为空*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_REVIEW_NULL_ERROR);
                return checkResult;
            }

            if (!RegexUtil.isPriorityQualified(review.priority)) {
                /*校验优先级是否超出范围*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_PRIORITY_ERROR);
                return checkResult;
            }
        }

        checkResult.put(IS_SUCCESS, "1");
        checkResult.put(MESSAGE, R.RequestMsg.SUCCESS);
        return checkResult;
    }
}