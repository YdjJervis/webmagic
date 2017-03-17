package com.eccang.cxf.followsell;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseReqParam;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.ValidateMsg;
import com.eccang.pojo.followsell.*;
import com.eccang.spider.amazon.monitor.GenerateFollowSellBatchMonitor;
import com.eccang.spider.amazon.pojo.Business;
import com.eccang.spider.amazon.pojo.crawl.FollowSell;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.amazon.pojo.relation.CustomerFollowSell;
import com.eccang.spider.amazon.pojo.relation.CustomerPayPackage;
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
    @Autowired
    private GenerateFollowSellBatchMonitor mGenerateFollowSellBatchMonitor;

    @Override
    public String addToMonitor(String json, boolean immediate) {

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

        /* 存储当前业务码 */
        String businessCode = immediate ? R.BusinessCode.IMMEDIATE_FOLLOW_SELL : R.BusinessCode.FOLLOW_SELL;

        CustomerBusiness customerBusiness;
        try {
            /* 业务及套餐限制验证 */
            Business business = mBusinessService.findByCode(businessCode);
            if (followSellReq.data.size() > business.getImportLimit()) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.BUSSINESS_LIMIT;
                return baseRspParam.toJson();
            }

            customerBusiness = mCustomerBusinessService.findByCode(followSellReq.customerCode, businessCode);
            if (followSellReq.data.size() > customerBusiness.maxData - customerBusiness.useData) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.PAY_PACKAGE_LIMIT;
                return baseRspParam.toJson();
            }
        } catch (Exception e) {
            serverException(baseRspParam, e);
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

                CustomerFollowSell cusFollowSell = getCustomerFollowSell(followSellReq, followSell, businessCode);
                if (mCustomerFollowSellService.isExist(cusFollowSell.customerCode, cusFollowSell.siteCode, cusFollowSell.asin)) {
                    ++crawledNum;
                }
                cusFollowSellList.add(cusFollowSell);
            }

            if (immediate) {
                mGenerateFollowSellBatchMonitor.generate(cusFollowSellList, true);
            } else {
                mCustomerFollowSellService.addAll(cusFollowSellList);
            }

            followSellRsp.data.totalCount = cusFollowSellList.size();
            followSellRsp.data.newCount = immediate ? 0 : cusFollowSellList.size() - crawledNum;
            followSellRsp.data.oldCount = immediate ? 0 : crawledNum;

            if (immediate) {
                followSellRsp.data.hasUsedNum = customerBusiness.useData + cusFollowSellList.size();
                followSellRsp.data.usableNum = customerBusiness.maxData - followSellRsp.data.hasUsedNum;

                /* 更新业务表 */
                customerBusiness.useData = followSellRsp.data.hasUsedNum;
                mCustomerBusinessService.update(customerBusiness);
            } else {
                /*对应客户下，跟卖监听业务的使用情况*/
                Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(followSellReq.customerCode, R.BusinessCode.FOLLOW_SELL);
                followSellRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
                followSellRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);
            }

        } catch (Exception e) {
            serverException(baseRspParam, e);
        }
        return followSellRsp.toJson();
    }

    private CustomerFollowSell getCustomerFollowSell(CusFollowSellAddReq followSellReq, CusFollowSellAddReq.FollowSell followSell, String businessCode) {
        /* 从套餐取出该客户该业务的一些默认配置 */
        CustomerPayPackage customerPayPackage = mCustomerPayPackageService.findActived(followSellReq.customerCode);
        PayPackageStub payPackageStub = mPayPackageStubService.find(customerPayPackage.packageCode, businessCode);

        CustomerFollowSell cusFollowSell = new CustomerFollowSell();
        cusFollowSell.customerCode = followSellReq.customerCode;
        cusFollowSell.siteCode = followSell.siteCode;
        cusFollowSell.asin = followSell.asin;
        cusFollowSell.sellerId = followSell.sellerId;
        cusFollowSell.priority = payPackageStub.priority;
        cusFollowSell.frequency = payPackageStub.frequency;
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

        try {
            /* 业务及套餐限制验证 */
            int reopenCount = 0;//重新打开的量 = 关闭状态调为打开的 - 打开状态调为关闭的
            for (CusFollowSellUpdateReq.FollowSell followSell : followSellUpdateReq.data) {

                CustomerFollowSell customerFollowSell = mCustomerFollowSellService.find(followSellUpdateReq.customerCode, followSell.siteCode, followSell.asin);

                if (customerFollowSell.crawl != followSell.crawl) {
                    if (customerFollowSell.crawl == 0 && followSell.crawl == 1) {
                        reopenCount++;
                    } else {
                        reopenCount--;
                    }
                }
            }

            /* 对业务限制量和套餐总量限制 */
            Business business = mBusinessService.findByCode(R.BusinessCode.FOLLOW_SELL);
            if (followSellUpdateReq.data.size() > business.getImportLimit()) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.BUSSINESS_LIMIT;
                return baseRspParam.toJson();
            }

            CustomerBusiness customerBusiness = mCustomerBusinessService.findByCode(followSellUpdateReq.customerCode, R.BusinessCode.FOLLOW_SELL);
            if (reopenCount > customerBusiness.maxData - customerBusiness.useData) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.PAY_PACKAGE_LIMIT;
                return baseRspParam.toJson();
            }
        } catch (Exception e) {
            serverException(baseRspParam, e);
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        CusFollowSellUpdateRsp followSellUpdateRsp = new CusFollowSellUpdateRsp();
        followSellUpdateRsp.customerCode = baseRspParam.customerCode;
        followSellUpdateRsp.status = baseRspParam.status;
        followSellUpdateRsp.msg = baseRspParam.msg;

        try {
            for (CusFollowSellUpdateReq.FollowSell followSell : followSellUpdateReq.data) {

                CustomerFollowSell customerFollowSell = getCustomerFollowSell(followSellUpdateReq, followSell, R.BusinessCode.FOLLOW_SELL);
                customerFollowSell.crawl = followSell.crawl;

                CustomerFollowSell dbCusFollowSell = mCustomerFollowSellService.find(baseRspParam.customerCode, followSell.siteCode, followSell.asin);
                if (dbCusFollowSell == null) {
                    continue;
                }

                if (customerFollowSell.crawl != dbCusFollowSell.crawl
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
                return getValidateMsg(false, R.RequestMsg.PARAMETER_DATA_NULL_ERROR);
            }

            for (CusFollowSellAddReq.FollowSell followSell : cusFollowSellAddReq.data) {
                if (!RegexUtil.isSiteCodeQualified(followSell.siteCode)) {
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR);
                }
                if (StringUtils.isEmpty(followSell.asin)) {
                    return getValidateMsg(false, R.RequestMsg.PARAMETER_KEYWORD_ASIN_ERROR);
                }
                if (baseReqParam instanceof CusFollowSellUpdateReq) {
                    if (!RegexUtil.isCrawlStatusQualified(followSell.crawl)) {
                        return getValidateMsg(false, R.RequestMsg.PARAMETER_STATUS_ERROR);
                    }
                }
            }

        } else if (baseReqParam instanceof FollowSellQueryReq) {

            FollowSellQueryReq followSellQueryReq = (FollowSellQueryReq) baseReqParam;

            if (CollectionUtils.isEmpty(followSellQueryReq.data)) {
                return getValidateMsg(false, R.RequestMsg.PARAMETER_DATA_NULL_ERROR);
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
