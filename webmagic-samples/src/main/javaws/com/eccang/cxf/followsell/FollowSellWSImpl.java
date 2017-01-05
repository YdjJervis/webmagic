package com.eccang.cxf.followsell;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.ValidateMsg;
import com.eccang.pojo.followsell.FollowSellReq;
import com.eccang.pojo.followsell.FollowSellRsp;
import com.eccang.pojo.followsell.FollowSellUpdateReq;
import com.eccang.pojo.followsell.FollowSellUpdateRsp;
import com.eccang.spider.amazon.pojo.relation.CustomerFollowSell;
import com.eccang.spider.amazon.service.crawl.FollowSellService;
import com.eccang.spider.amazon.service.relation.CustomerFollowSellService;
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
    private FollowSellService mService;
    @Autowired
    private CustomerFollowSellService mCustomerFollowSellService;

    @Override
    public String addToMonitor(String json) {

        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        FollowSellReq followSellReq = parseRequestParam(json, baseRspParam, FollowSellReq.class);
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
        FollowSellRsp followSellRsp = new FollowSellRsp();
        followSellRsp.customerCode = followSellReq.cutomerCode;
        followSellRsp.status = baseRspParam.status;
        followSellRsp.msg = baseRspParam.msg;

        try {
            int crawledNum = 0;
            /* 把客户和跟卖的关系保存起来 */
            List<CustomerFollowSell> cusFollowSellList = new ArrayList<>();
            for (FollowSellReq.FollowSell followSell : followSellReq.data) {

                CustomerFollowSell cusFollowSell = getCustomerFollowSell(followSellReq, followSell);

                cusFollowSellList.add(cusFollowSell);
            }

            mCustomerFollowSellService.addAll(cusFollowSellList);

            followSellRsp.data.totalCount = cusFollowSellList.size();
            followSellRsp.data.newCount = cusFollowSellList.size() - crawledNum;
            followSellRsp.data.oldCount = crawledNum;

            /*对应客户下，keywordRank监听业务的使用情况*/
            Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(followSellReq.cutomerCode, R.BusinessCode.FOLLOW_SELL);
            followSellRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
            followSellRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);
        } catch (Exception e) {
            serverException(baseRspParam, e);
        }
        return followSellRsp.toJson();
    }

    private CustomerFollowSell getCustomerFollowSell(FollowSellReq followSellReq, FollowSellReq.FollowSell followSell) {
        CustomerFollowSell cusFollowSell = new CustomerFollowSell();
        cusFollowSell.customerCode = followSellReq.cutomerCode;
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

        FollowSellUpdateReq followSellUpdateReq = parseRequestParam(json, baseRspParam, FollowSellUpdateReq.class);
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
        FollowSellUpdateRsp followSellUpdateRsp = new FollowSellUpdateRsp();
        followSellUpdateRsp.customerCode = baseRspParam.customerCode;
        followSellUpdateRsp.status = baseRspParam.status;
        followSellUpdateRsp.msg = baseRspParam.msg;

        try {
            for (FollowSellUpdateReq.FollowSell followSell : followSellUpdateReq.data) {

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
        return null;
    }

    @Override
    public String getFollowSellList(String json) {
        return null;
    }

    private ValidateMsg checkData(FollowSellReq followSellReq) {

        if (CollectionUtils.isEmpty(followSellReq.data)) {
            return getValidateMsg(false, R.RequestMsg.PARAMETER_ASIN_NULL_ERROR);
        }

        for (FollowSellReq.FollowSell followSell : followSellReq.data) {
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

        return getValidateMsg(true, R.RequestMsg.SUCCESS);
    }
}
