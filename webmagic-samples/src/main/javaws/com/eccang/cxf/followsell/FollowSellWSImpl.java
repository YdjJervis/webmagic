package com.eccang.cxf.followsell;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseReqParam;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.ValidateMsg;
import com.eccang.pojo.followsell.*;
import com.eccang.spider.amazon.pojo.crawl.FollowSell;
import com.eccang.spider.amazon.pojo.relation.CustomerFollowSell;
import com.eccang.spider.amazon.service.crawl.FollowSellService;
import com.eccang.spider.amazon.service.relation.CustomerFollowSellService;
import com.eccang.spider.amazon.util.DateUtils;
import com.eccang.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 跟卖WebService接口实现类
 * @date 2017/1/5 14:28
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class FollowSellWSImpl extends AbstractSpiderWS implements FollowSellWS {

    @Autowired
    private FollowSellService mFollowSellService;
    @Autowired
    private CustomerFollowSellService mCustomerFollowSellService;

    @Override
    public String addToMonitor(String json) {

        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusFollowSellAddReq followSellReq = parseRequestParam(json, baseRspParam, CusFollowSellAddReq.class);
        if (followSellReq == null) {
            return baseRspParam.toJson();
        }

        /*  参数验证阶段 */
        ValidateMsg validateMsg = checkData(followSellReq);
        if (!validateMsg.isSuccess) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = validateMsg.msg;
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        CusFollowSellAddRsp followSellRsp = new CusFollowSellAddRsp();
        followSellRsp.customerCode = followSellReq.customerCode;
        followSellRsp.status = baseRspParam.status;
        followSellRsp.msg = baseRspParam.msg;

        try {
            int crawledNum = 0;
            /* 把客户和跟卖的关系保存起来 */
            List<CustomerFollowSell> cusFollowSellList = new ArrayList<>();
            for (CusFollowSellAddReq.FollowSell followSell : followSellReq.data) {

                CustomerFollowSell cusFollowSell = getCustomerFollowSell(followSellReq, followSell);

                cusFollowSellList.add(cusFollowSell);
            }

            mCustomerFollowSellService.addAll(cusFollowSellList);

            followSellRsp.data.totalCount = cusFollowSellList.size();
            followSellRsp.data.newCount = cusFollowSellList.size() - crawledNum;
            followSellRsp.data.oldCount = crawledNum;

            /*对应客户下，keywordRank监听业务的使用情况*/
            Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(followSellReq.customerCode, R.BusinessCode.FOLLOW_SELL);
            followSellRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
            followSellRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);
        } catch (Exception e) {
            serverException(baseRspParam, e);
        }
        return followSellRsp.toJson();
    }

    private CustomerFollowSell getCustomerFollowSell(CusFollowSellAddReq followSellReq, CusFollowSellAddReq.FollowSell followSell) {
        CustomerFollowSell cusFollowSell = new CustomerFollowSell();
        cusFollowSell.customerCode = followSellReq.customerCode;
        cusFollowSell.siteCode = followSell.siteCode;
        cusFollowSell.asin = followSell.asin;
        cusFollowSell.sellerId = followSell.sellerId;
        cusFollowSell.priority = followSell.priority;
        cusFollowSell.frequency = followSell.frequency;
        return cusFollowSell;
    }

    @Override
    public String setStatus(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusFollowSellUpdateReq followSellUpdateReq = parseRequestParam(json, baseRspParam, CusFollowSellUpdateReq.class);
        if (followSellUpdateReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        ValidateMsg msg = checkData(followSellUpdateReq);
        if (!msg.isSuccess) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = msg.msg;
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        CusFollowSellUpdateRsp followSellUpdateRsp = new CusFollowSellUpdateRsp();
        followSellUpdateRsp.customerCode = baseRspParam.customerCode;
        followSellUpdateRsp.status = baseRspParam.status;
        followSellUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CusFollowSellUpdateReq.FollowSell followSell : followSellUpdateReq.data) {

                CustomerFollowSell customerFollowSell = getCustomerFollowSell(followSellUpdateReq, followSell);
                customerFollowSell.crawl = followSell.crawl;

                CustomerFollowSell dbCusFollowSell = mCustomerFollowSellService.find(baseRspParam.customerCode, followSell.siteCode, followSell.asin);
                if (dbCusFollowSell == null) {
                    continue;
                }

                if (customerFollowSell.crawl != dbCusFollowSell.crawl
                        || customerFollowSell.frequency != dbCusFollowSell.frequency
                        || customerFollowSell.priority != dbCusFollowSell.priority
                        || !customerFollowSell.sellerId.equals(dbCusFollowSell.sellerId)) {
                    followSellUpdateRsp.data.changed++;
                    mCustomerFollowSellService.update(customerFollowSell);
                }

            }
            followSellUpdateRsp.data.noChange = followSellUpdateReq.data.size() - followSellUpdateRsp.data.changed;

            /*对应客户下，keywordRank监听业务的使用情况*/
            Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(baseRspParam.customerCode, R.BusinessCode.FOLLOW_SELL);
            followSellUpdateRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
            followSellUpdateRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);
        } catch (Exception e) {
            serverException(followSellUpdateRsp, e);
        }

        return followSellUpdateRsp.toJson();
    }

    @Override
    public String getMonitorList(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusFollowSellQueryReq cusFollowSellQueryReq = parseRequestParam(json, baseRspParam, CusFollowSellQueryReq.class);
        if (cusFollowSellQueryReq == null) {
            return baseRspParam.toJson();
        }

        CusFollowSellQueryRsp cusFollowSellQueryRsp = new CusFollowSellQueryRsp();
        cusFollowSellQueryRsp.customerCode = baseRspParam.customerCode;
        cusFollowSellQueryRsp.status = baseRspParam.status;
        cusFollowSellQueryRsp.msg = baseRspParam.msg;

        try {
            /*查询数据并封闭到返回对象里*/
            CusFollowSellQueryRsp.CusFollowSell cusFollowSell;

            List<CustomerFollowSell> cFollowSells = mCustomerFollowSellService.findByCustomer(baseRspParam.customerCode);
            for (CustomerFollowSell cFollowSell : cFollowSells) {
                cusFollowSell = cusFollowSellQueryRsp.new CusFollowSell();
                cusFollowSell.siteCode = cFollowSell.siteCode;
                cusFollowSell.asin = cFollowSell.asin;
                cusFollowSell.sellerId = cFollowSell.sellerId;
                cusFollowSell.crawl = cFollowSell.crawl;
                cusFollowSell.priority = cFollowSell.priority;
                cusFollowSell.frequency = cFollowSell.frequency;
                cusFollowSell.onSell = cFollowSell.onSell;
                cusFollowSell.extra = cFollowSell.extra;
                cusFollowSell.syncTime = DateUtils.format(cFollowSell.syncTime);
                cusFollowSell.createTime = DateUtils.format(cFollowSell.createTime);
                cusFollowSell.updateTime = DateUtils.format(cFollowSell.updateTime);
                cusFollowSellQueryRsp.data.add(cusFollowSell);
            }
        } catch (Exception e) {
            serverException(cusFollowSellQueryRsp, e);
        }

        return cusFollowSellQueryRsp.toJson();
    }

    @Override
    public String getFollowSellList(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        FollowSellQueryReq followSellQueryReq = parseRequestParam(json, baseRspParam, FollowSellQueryReq.class);
        if (followSellQueryReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        ValidateMsg msg = checkData(followSellQueryReq);
        if (!msg.isSuccess) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = msg.msg;
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        FollowSellQueryRsp followSellQueryRsp = new FollowSellQueryRsp();
        followSellQueryRsp.customerCode = baseRspParam.customerCode;
        followSellQueryRsp.status = baseRspParam.status;
        followSellQueryRsp.msg = baseRspParam.msg;

        try {
            for (FollowSellQueryReq.FollowSell followSellParams : followSellQueryReq.data) {
                List<FollowSell> dbFollowSellList = mFollowSellService.findAll(new FollowSell(followSellParams.batchNum, followSellParams.siteCode, followSellParams.asin));

                if (CollectionUtils.isEmpty(dbFollowSellList)) {
                    continue;
                }

                FollowSellQueryRsp.FollowSellCommon followSellCommon = followSellQueryRsp.new FollowSellCommon();
                followSellCommon.batchNum = followSellParams.batchNum;
                followSellCommon.siteCode = followSellParams.siteCode;
                followSellCommon.asin = followSellParams.asin;

                for (FollowSell dbFollowSell : dbFollowSellList) {

                    FollowSellQueryRsp.FollowSellCommon.FollowSell followSell = followSellCommon.new FollowSell();

                    followSell.sellerId = dbFollowSell.sellerID;
                    followSell.price = dbFollowSell.price;
                    followSell.transPolicy = dbFollowSell.transPolicy;
                    followSell.condition = dbFollowSell.condition;
                    followSell.rating = dbFollowSell.rating;
                    followSell.probability = dbFollowSell.probability;
                    followSell.starLevel = dbFollowSell.starLevel;
                    followSell.createTime = DateUtils.format(dbFollowSell.createTime);
                    followSell.updateTime = DateUtils.format(dbFollowSell.updateTime);

                    followSellCommon.list.add(followSell);
                }

                followSellQueryRsp.data.add(followSellCommon);
            }

        } catch (Exception e) {
            serverException(followSellQueryRsp, e);
        }

        return followSellQueryRsp.toJson();
    }

    private ValidateMsg checkData(BaseReqParam baseReqParam) {

        if (baseReqParam instanceof CusFollowSellAddReq) {
            CusFollowSellAddReq cusFollowSellAddReq = (CusFollowSellAddReq) baseReqParam;

            if (CollectionUtils.isEmpty(cusFollowSellAddReq.data)) {
                return getValidateMsg(false, R.RequestMsg.PARAMETER_ASIN_NULL_ERROR);
            }

            for (CusFollowSellAddReq.FollowSell followSell : cusFollowSellAddReq.data) {
                if (!RegexUtil.isSiteCodeQualified(followSell.siteCode)) {
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR);
                }
                if (StringUtils.isEmpty(followSell.asin)) {
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_KEYWORD_ASIN_ERROR);
                }
                if (!RegexUtil.isFrequencyQualified(followSell.frequency)) {
                    /*校验爬取频率是否在取值范围内*/
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_REVIEW_FREQUENCY_ERROR);
                }
                if (!RegexUtil.isPriorityQualified(followSell.priority)) {
                    /*校验优先级是否超出范围*/
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_ASIN_PRIORITY_ERROR);
                }
            }
        } else if (baseReqParam instanceof FollowSellQueryReq) {

            FollowSellQueryReq followSellQueryReq = (FollowSellQueryReq) baseReqParam;

            if (CollectionUtils.isEmpty(followSellQueryReq.data)) {
                return getValidateMsg(false, R.RequestMsg.PARAMETER_ASIN_NULL_ERROR);
            }

            for (FollowSellQueryReq.FollowSell followSell : followSellQueryReq.data) {
                if (!RegexUtil.isBatchNumQualified(followSell.batchNum)) {
                    return getValidateMsg(false, R.RequestMsg.BATCH_NUM_WRONG);
                }
                if (!RegexUtil.isSiteCodeQualified(followSell.siteCode)) {
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR);
                }
                if (StringUtils.isEmpty(followSell.asin)) {
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_KEYWORD_ASIN_ERROR);
                }
            }

        }

        return getValidateMsg(true, R.RequestMsg.SUCCESS);
    }
}
