package com.eccang.cxf.review;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.review.*;
import com.eccang.spider.amazon.pojo.Business;
import com.eccang.spider.amazon.pojo.crawl.Review;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.amazon.pojo.relation.*;
import com.eccang.spider.amazon.service.crawl.ReviewService;
import com.eccang.spider.amazon.service.relation.AsinRootAsinService;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.eccang.spider.amazon.service.relation.CustomerReviewService;
import com.eccang.spider.amazon.util.DateUtils;
import com.eccang.util.RegexUtil;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

        ReviewReq reviewReq = parseRequestParam(json, baseRspParam, ReviewReq.class);
        if (reviewReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        Map<String, String> checkResult = checkData(reviewReq.data);
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        try {
            /* 业务及套餐限制验证 */
            Business business = mBusinessService.findByCode(R.BusinessCode.MONITOR_SPIDER);
            if (reviewReq.data.size() > business.getImportLimit()) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.BUSSINESS_LIMIT;
                return baseRspParam.toJson();
            }

            CustomerBusiness customerBusiness = mCustomerBusinessService.findByCode(reviewReq.customerCode, R.BusinessCode.MONITOR_SPIDER);
            if (reviewReq.data.size() > customerBusiness.maxData - customerBusiness.useData) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.PAY_PACKAGE_LIMIT;
                return baseRspParam.toJson();
            }
        } catch (Exception e) {
            serverException(baseRspParam, e);
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        ReviewRsp reviewRsp = new ReviewRsp();
        reviewRsp.customerCode = reviewReq.customerCode;
        reviewRsp.status = baseRspParam.status;
        reviewRsp.msg = baseRspParam.msg;

        try {
            /* 从套餐取出该客户该业务的一些默认配置 */
            CustomerPayPackage customerPayPackage = mCustomerPayPackageService.findActived(reviewReq.customerCode);
            PayPackageStub payPackageStub = mPayPackageStubService.find(customerPayPackage.packageCode, R.BusinessCode.MONITOR_SPIDER);

            int crawledNum = 0;
            /* 把客户和Review的关系保存起来 */
            List<CustomerReview> customerReviewList = new ArrayList<>();
            for (ReviewReq.Review review : reviewReq.data) {
                CustomerReview customerReview = new CustomerReview();
                customerReview.customerCode = reviewReq.customerCode;
                customerReview.siteCode = review.siteCode;
                customerReview.asin = review.asin;
                customerReview.reviewId = review.reviewId;
                customerReview.priority = payPackageStub.priority;
                customerReview.frequency = payPackageStub.frequency;
                if (mCustomerReviewService.isExist(reviewReq.customerCode, review.reviewId)) {
                    crawledNum++;
                }
                customerReviewList.add(customerReview);
            }
            mCustomerReviewService.addAll(customerReviewList);

            reviewRsp.data.totalCount = customerReviewList.size();
            reviewRsp.data.newCount = customerReviewList.size() - crawledNum;
            reviewRsp.data.oldCount = crawledNum;
        } catch (Exception e) {
            serverException(reviewRsp, e);
        }
        /*对应客户下，review监听业务的使用情况*/
        Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(reviewReq.customerCode, R.BusinessCode.MONITOR_SPIDER);
        reviewRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
        reviewRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);

        return reviewRsp.toJson();
    }

    @Override
    public String getReviewsByAsin(String asinJson) {

        BaseRspParam baseRspParam = auth(asinJson);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        ReviewQueryReq reviewQueryReq = parseRequestParam(asinJson, baseRspParam, ReviewQueryReq.class);
        if (reviewQueryReq == null) {
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
        reviewQueryRsp.customerCode = baseRspParam.customerCode;
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

            CustomerAsin customerAsin = new CustomerAsin(baseRspParam.customerCode, siteCode, asin, 0);
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
            serverException(reviewQueryRsp, e);
        }

        return reviewQueryRsp.toJson();
    }

    @Override
    public String getReviewById(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        ReviewQueryReq reviewQueryReq = parseRequestParam(json, baseRspParam, ReviewQueryReq.class);
        if (reviewQueryReq == null) {
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
        reviewQueryRsp.customerCode = baseRspParam.customerCode;
        reviewQueryRsp.status = baseRspParam.status;
        reviewQueryRsp.msg = baseRspParam.msg;

        try {

            CustomerAsin customerAsin = new CustomerAsin(baseRspParam.customerCode, siteCode, asin, 0);
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
            serverException(reviewQueryRsp, e);
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
        resultReview.version = StringUtils.isEmpty(review.version) ? "" : review.version;
        resultReview.star = review.star;
        resultReview.title = review.title;
        resultReview.content = review.content;
    }

    /**
     * 设置监听review的优先级
     */
    @Deprecated
    @Override
    public String setPriority(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusReviewUpdateReq cusReviewUpdateReq = parseRequestParam(json, baseRspParam, CusReviewUpdateReq.class);
        if (cusReviewUpdateReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(cusReviewUpdateReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;
            return baseRspParam.toJson();
        }

        for (CusReviewUpdateReq.CustomerReview customerReview : cusReviewUpdateReq.data) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;

            if (customerReview == null) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR;
                return baseRspParam.toJson();
            }

            if (!RegexUtil.isReviewIdQualified(customerReview.reviewId)) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_REVIEW_NULL_ERROR;
                return baseRspParam.toJson();
            }

            /*if (!RegexUtil.isPriorityQualified(customerReview.priority)) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_PRIORITY_ERROR;
                return baseRspParam.toJson();
            }*/
            baseRspParam.status = R.HttpStatus.SUCCESS;
        }

        CusReviewUpdateRsp cusReviewUpdateRsp = new CusReviewUpdateRsp();
        cusReviewUpdateRsp.customerCode = cusReviewUpdateReq.customerCode;
        cusReviewUpdateRsp.status = baseRspParam.status;
        cusReviewUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CusReviewUpdateReq.CustomerReview customerReview : cusReviewUpdateReq.data) {
                CustomerReview data = mCustomerReviewService.findCustomerReview(baseRspParam.customerCode, customerReview.reviewId);
                /*if (customerReview.priority == data.priority) {
                    cusReviewUpdateRsp.data.noChange++;
                } else {
                    data.priority = customerReview.priority;
                    mCustomerReviewService.update(data);
                    cusReviewUpdateRsp.data.changed++;
                }*/
            }
        } catch (Exception e) {
            serverException(cusReviewUpdateRsp, e);
        }
        return cusReviewUpdateRsp.toJson();
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

        CusReviewUpdateReq cusReviewUpdateReq = parseRequestParam(json, baseRspParam, CusReviewUpdateReq.class);
        if (cusReviewUpdateReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(cusReviewUpdateReq.data)) {
            /*校验传入的请求是否为null*/
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;
            return baseRspParam.toJson();
        }

        for (CusReviewUpdateReq.CustomerReview customerReview : cusReviewUpdateReq.data) {
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

        try {
            /* 业务及套餐限制验证 */
            int reopenCount = 0;//重新打开的量 = 关闭状态调为打开的 - 打开状态调为关闭的
            for (CusReviewUpdateReq.CustomerReview cr : cusReviewUpdateReq.data) {
                CustomerReview customerReview = mCustomerReviewService.findCustomerReview(cusReviewUpdateReq.customerCode, cr.reviewId);

                if (customerReview.crawl != cr.crawl) {
                    if (customerReview.crawl == 0 && cr.crawl == 1) {
                        reopenCount++;
                    } else {
                        reopenCount--;
                    }
                }
            }

            /* 对业务限制量和套餐总量限制 */
            Business business = mBusinessService.findByCode(R.BusinessCode.MONITOR_SPIDER);
            if (cusReviewUpdateReq.data.size() > business.getImportLimit()) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.BUSSINESS_LIMIT;
                return baseRspParam.toJson();
            }

            CustomerBusiness customerBusiness = mCustomerBusinessService.findByCode(cusReviewUpdateReq.customerCode, R.BusinessCode.MONITOR_SPIDER);
            if (reopenCount > customerBusiness.maxData - customerBusiness.useData) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.PAY_PACKAGE_LIMIT;
                return baseRspParam.toJson();
            }
        } catch (Exception e) {
            serverException(baseRspParam, e);
            return baseRspParam.toJson();
        }

        CusReviewUpdateRsp cusReviewUpdateRsp = new CusReviewUpdateRsp();
        cusReviewUpdateRsp.customerCode = cusReviewUpdateReq.customerCode;
        cusReviewUpdateRsp.status = baseRspParam.status;
        cusReviewUpdateRsp.msg = baseRspParam.msg;

        /* 逻辑处理阶段 */
        try {
            for (CusReviewUpdateReq.CustomerReview customerReview : cusReviewUpdateReq.data) {
                CustomerReview cr = mCustomerReviewService.findCustomerReview(baseRspParam.customerCode, customerReview.reviewId);

                if (cr == null) {
                    cusReviewUpdateRsp.msg = R.RequestMsg.PARAMETER_REVIEW_EMPTY__ERROR;
                    return cusReviewUpdateRsp.toJson();
                }

                if (customerReview.crawl == cr.crawl) {
                    cusReviewUpdateRsp.data.noChange++;
                } else {
                    cr.crawl = customerReview.crawl;
                    mCustomerReviewService.update(cr);
                    cusReviewUpdateRsp.data.changed++;
                }
            }
        } catch (Exception e) {
            serverException(cusReviewUpdateRsp, e);
        }

        /*对应客户下，review监听业务的使用情况*/
        Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(baseRspParam.customerCode, R.BusinessCode.MONITOR_SPIDER);
        cusReviewUpdateRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
        cusReviewUpdateRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);

        return cusReviewUpdateRsp.toJson();
    }

    /**
     * 更新监听Review的监听频率（h/次）
     */
    @Deprecated
    @Override
    public String setFrequency(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusReviewUpdateReq cusReviewUpdateReq = parseRequestParam(json, baseRspParam, CusReviewUpdateReq.class);
        if (cusReviewUpdateReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(cusReviewUpdateReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;
            return baseRspParam.toJson();
        }

        for (CusReviewUpdateReq.CustomerReview customerReview : cusReviewUpdateReq.data) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;

            if (customerReview == null) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR;
                return baseRspParam.toJson();
            }

            if (!RegexUtil.isReviewIdQualified(customerReview.reviewId)) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_REVIEW_NULL_ERROR;
                return baseRspParam.toJson();
            }

            /*if (!RegexUtil.isFrequencyQualified(customerReview.frequency)) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_REVIEW_FREQUENCY_ERROR;
                return baseRspParam.toJson();
            }*/
            baseRspParam.status = R.HttpStatus.SUCCESS;
        }

        CusReviewUpdateRsp cusReviewUpdateRsp = new CusReviewUpdateRsp();
        cusReviewUpdateRsp.customerCode = cusReviewUpdateReq.customerCode;
        cusReviewUpdateRsp.status = baseRspParam.status;
        cusReviewUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CusReviewUpdateReq.CustomerReview customerReview : cusReviewUpdateReq.data) {
                CustomerReview data = mCustomerReviewService.findCustomerReview(baseRspParam.customerCode, customerReview.reviewId);

                if (data == null) {
                    cusReviewUpdateRsp.msg = R.RequestMsg.PARAMETER_REVIEW_EMPTY__ERROR;
                    return cusReviewUpdateRsp.toJson();
                }

                /*if (customerReview.frequency == data.frequency) {
                    cusReviewUpdateRsp.data.noChange++;
                } else {
                    data.frequency = customerReview.frequency;
                    mCustomerReviewService.update(data);
                    cusReviewUpdateRsp.data.changed++;
                }*/
            }
        } catch (Exception e) {
            serverException(cusReviewUpdateRsp, e);
        }
        return cusReviewUpdateRsp.toJson();
    }

    /**
     * 更新更新监听Review的状态
     */
    public String updateCustomerReview(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusReviewUpdateReq cusReviewUpdateReq = parseRequestParam(json, baseRspParam, CusReviewUpdateReq.class);
        if (cusReviewUpdateReq == null) {
            return baseRspParam.toJson();
        }
        /* 参数验证阶段 */
        boolean isParamQualified = true;
        if (CollectionUtils.isEmpty(cusReviewUpdateReq.data)) {
            isParamQualified = false;
        }
        for (CusReviewUpdateReq.CustomerReview customerReview : cusReviewUpdateReq.data) {
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

        CusReviewUpdateRsp cusReviewUpdateRsp = new CusReviewUpdateRsp();
        cusReviewUpdateRsp.customerCode = cusReviewUpdateReq.customerCode;
        cusReviewUpdateRsp.status = baseRspParam.status;
        cusReviewUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CusReviewUpdateReq.CustomerReview customerReview : cusReviewUpdateReq.data) {
                CustomerReview data = mCustomerReviewService.findCustomerReview(baseRspParam.customerCode, customerReview.reviewId);
                /*if (customerReview.frequency == data.frequency &&
                        customerReview.priority == data.priority &&
                        customerReview.crawl == data.crawl) {
                    cusReviewUpdateRsp.data.noChange++;
                } else {
                    data.priority = customerReview.priority;
                    data.frequency = customerReview.frequency;
                    data.crawl = customerReview.crawl;
                    mCustomerReviewService.update(data);
                    cusReviewUpdateRsp.data.changed++;
                }*/
            }
        } catch (Exception e) {
            serverException(cusReviewUpdateRsp, e);
        }
        return cusReviewUpdateRsp.toJson();
    }

    @Override
    public String getReviewsStatus(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusReviewRsp cusReviewRsp = new CusReviewRsp();
        cusReviewRsp.customerCode = baseRspParam.customerCode;
        cusReviewRsp.status = baseRspParam.status;
        cusReviewRsp.msg = baseRspParam.msg;

        cusReviewRsp.data = new ArrayList<>();

        List<CustomerReview> customerReviewList = mCustomerReviewService.findCustomerReviewsByCustomerCode(baseRspParam.customerCode);
        CusReviewRsp.Review review;
        for (CustomerReview customerReview : customerReviewList) {
            review = cusReviewRsp.new Review();
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
            cusReviewRsp.data.add(review);

        }

        return cusReviewRsp.toJson();
    }

    /**
     * 校验数据
     */
    private Map<String, String> checkData(List<ReviewReq.Review> reviews) {
        Map<String, String> checkResult = new HashMap<>();

        checkResult.put(IS_SUCCESS, "0");
        if (CollectionUtils.isEmpty(reviews)) {
            /*校验请求数据列表是否为空*/
            checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_DATA_NULL_ERROR);
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

            if (!RegexUtil.isReviewIdQualified(review.reviewId)) {
                /*校验reviewId是否为空*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_REVIEW_NULL_ERROR);
                return checkResult;
            }
        }

        checkResult.put(IS_SUCCESS, "1");
        checkResult.put(MESSAGE, R.RequestMsg.SUCCESS);
        return checkResult;
    }
}