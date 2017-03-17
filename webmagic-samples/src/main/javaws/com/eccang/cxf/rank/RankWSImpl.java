package com.eccang.cxf.rank;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.rank.*;
import com.eccang.spider.amazon.monitor.GenerateKeywordRankBatchMonitor;
import com.eccang.spider.amazon.pojo.Business;
import com.eccang.spider.amazon.pojo.crawl.GoodsRankInfo;
import com.eccang.spider.amazon.pojo.crawl.KeywordRank;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.amazon.pojo.relation.CustomerKeywordRank;
import com.eccang.spider.amazon.pojo.relation.CustomerPayPackage;
import com.eccang.spider.amazon.service.crawl.GoodsRankInfoService;
import com.eccang.spider.amazon.service.crawl.KeywordRankService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.eccang.spider.amazon.service.relation.CustomerKeywordRankService;
import com.eccang.spider.amazon.util.DateUtils;
import com.eccang.util.RegexUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hardy
 * @version V0.2
 *          关键词排名搜索webservice
 *          2017/1/2 14:01
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class RankWSImpl extends AbstractSpiderWS implements RankWS {

    @Autowired
    private CustomerKeywordRankService mCustomerKeywordRankService;
    @Autowired
    private CustomerBusinessService mCustomerBusinessService;
    @Autowired
    private KeywordRankService mKeywordRankService;
    @Autowired
    private GoodsRankInfoService mGoodsRankInfoService;
    @Autowired
    private GenerateKeywordRankBatchMonitor mGenerateKeywordRankBatchMonitor;

    @Override
    public String addToMonitor(String json, boolean immediate) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        RankReq rankReq = parseRequestParam(json, baseRspParam, RankReq.class);
        if (rankReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        Map<String, String> checkResult = checkData(rankReq.getData());
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        /* 存储当前业务码 */
        String businessCode = immediate ? R.BusinessCode.IMMEDIATE_KEYWORD_RANK_SPIDER : R.BusinessCode.KEYWORD_RANK_SPIDER;

        /* 业务，套餐验证 */
        CustomerBusiness customerBusiness = getCusBusAndValidate(baseRspParam, businessCode, rankReq.data.size());
        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        RankRsp rankRsp = new RankRsp();
        rankRsp.customerCode = rankReq.customerCode;
        rankRsp.status = baseRspParam.status;
        rankRsp.msg = baseRspParam.msg;

        try {
            /* 从套餐取出该客户该业务的一些默认配置 */
            CustomerPayPackage customerPayPackage = mCustomerPayPackageService.findActived(rankReq.customerCode);
            PayPackageStub payPackageStub = mPayPackageStubService.find(customerPayPackage.packageCode, businessCode);

            int crawledNum = 0;
            /* 把客户和keywordRank的关系保存起来 */
            List<CustomerKeywordRank> customerKeywordRanks = new ArrayList<>();
            for (RankReq.KeywordRank keywordRank : rankReq.getData()) {
                CustomerKeywordRank customerKeywordRank = new CustomerKeywordRank();
                customerKeywordRank.customerCode = rankReq.customerCode;
                customerKeywordRank.siteCode = keywordRank.siteCode;
                customerKeywordRank.asin = keywordRank.asin;
                customerKeywordRank.keyword = keywordRank.keyword;
                customerKeywordRank.departmentCode = keywordRank.departmentCode;
                customerKeywordRank.priority = payPackageStub.priority;
                customerKeywordRank.frequency = payPackageStub.frequency;

                if (mCustomerKeywordRankService.isExist(customerKeywordRank)) {
                    crawledNum++;
                }

                customerKeywordRanks.add(customerKeywordRank);
            }

            if (immediate) {
                mGenerateKeywordRankBatchMonitor.generate(customerKeywordRanks, true);
            } else {
                mCustomerKeywordRankService.addAll(customerKeywordRanks);
            }

            rankRsp.data.totalCount = customerKeywordRanks.size();
            rankRsp.data.newCount = immediate ? 0 : customerKeywordRanks.size() - crawledNum;
            rankRsp.data.oldCount = immediate ? 0 : crawledNum;

            if (immediate) {
                rankRsp.data.hasUsedNum = customerBusiness.useData + customerKeywordRanks.size();
                rankRsp.data.usableNum = customerBusiness.maxData - rankRsp.data.hasUsedNum;

                /* 更新业务表 */
                customerBusiness.useData = rankRsp.data.hasUsedNum;
                mCustomerBusinessService.update(customerBusiness);
            } else {
                /*对应客户下，keywordRank监听业务的使用情况*/
                Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(rankReq.customerCode, businessCode);
                rankRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
                rankRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);
            }

        } catch (Exception e) {
            serverException(rankRsp, e);
        }

        return rankRsp.toJson();
    }

    @Override
    public String setStatus(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusRankUpdateReq cusRankUpdateReq = parseRequestParam(json, baseRspParam, CusRankUpdateReq.class);
        if (cusRankUpdateReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(cusRankUpdateReq.getData())) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;
            return baseRspParam.toJson();
        }

        Map<String, String> checkResult = checkData(cusRankUpdateReq.getData());
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        for (RankReq.KeywordRank keywordRank : cusRankUpdateReq.getData()) {

            if (!RegexUtil.isCrawlStatusQualified(keywordRank.crawl)) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_STATUS_ERROR;
                return baseRspParam.toJson();
            }
        }

        try {
            /* 业务及套餐限制验证 */
            int reopenCount = 0;//重新打开的量 = 关闭状态调为打开的 - 打开状态调为关闭的
            for (CusRankUpdateReq.KeywordRank cr : cusRankUpdateReq.data) {
                CustomerKeywordRank ckr = new CustomerKeywordRank();
                ckr.asin = cr.asin;
                ckr.keyword = cr.keyword;
                ckr.customerCode = cusRankUpdateReq.customerCode;
                ckr.siteCode = cr.siteCode;
                ckr.departmentCode = cr.departmentCode;

                CustomerKeywordRank customerKeywordRank = mCustomerKeywordRankService.findByObj(ckr);

                if (customerKeywordRank.crawl != cr.crawl) {
                    if (customerKeywordRank.crawl == 0 && cr.crawl == 1) {
                        reopenCount++;
                    } else {
                        reopenCount--;
                    }
                }
            }

            /* 对业务限制量和套餐总量限制 */
            Business business = mBusinessService.findByCode(R.BusinessCode.KEYWORD_RANK_SPIDER);
            if (cusRankUpdateReq.data.size() > business.getImportLimit()) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.BUSSINESS_LIMIT;
                return baseRspParam.toJson();
            }

            CustomerBusiness customerBusiness = mCustomerBusinessService.findByCode(cusRankUpdateReq.customerCode, R.BusinessCode.KEYWORD_RANK_SPIDER);
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
        CusRankUpdateRsp cusRankUpdateRsp = new CusRankUpdateRsp();
        cusRankUpdateRsp.customerCode = cusRankUpdateReq.customerCode;
        cusRankUpdateRsp.status = baseRspParam.status;
        cusRankUpdateRsp.msg = baseRspParam.msg;

        try {
            CustomerKeywordRank customerKeywordRank;
            for (RankReq.KeywordRank keywordRank : cusRankUpdateReq.getData()) {
                customerKeywordRank = new CustomerKeywordRank();
                customerKeywordRank.customerCode = cusRankUpdateReq.customerCode;
                customerKeywordRank.siteCode = keywordRank.siteCode;
                customerKeywordRank.asin = keywordRank.asin;
                customerKeywordRank.keyword = keywordRank.keyword;
                customerKeywordRank.departmentCode = keywordRank.departmentCode;
                CustomerKeywordRank cKeywordRank = mCustomerKeywordRankService.findByObj(customerKeywordRank);

                if (cKeywordRank == null) {
                    cusRankUpdateRsp.msg = R.RequestMsg.PARAMETER_KEYWORD_EMPTY__ERROR;
                    return cusRankUpdateRsp.toJson();
                }

                if (keywordRank.crawl == cKeywordRank.crawl) {
                    cusRankUpdateRsp.data.noChange++;
                } else {
                    cKeywordRank.crawl = keywordRank.crawl;
                    mCustomerKeywordRankService.update(cKeywordRank);
                    cusRankUpdateRsp.data.changed++;
                }
            }

            /*对应客户下，keywordRank监听业务的使用情况*/
            Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(cusRankUpdateReq.customerCode, R.BusinessCode.KEYWORD_RANK_SPIDER);
            cusRankUpdateRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
            cusRankUpdateRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);
        } catch (Exception e) {
            serverException(cusRankUpdateRsp, e);
        }

        return cusRankUpdateRsp.toJson();
    }

    @Override
    public String getKeywordRankInfo(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        RankQueryReq rankQueryReq = parseRequestParam(json, baseRspParam, RankQueryReq.class);
        if (rankQueryReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (rankQueryReq.getData() == null) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;
            return baseRspParam.toJson();
        }

        RankQueryReq.KeywordInfo keywordInfo = rankQueryReq.getData();
        if (StringUtils.isEmpty(keywordInfo.batchNum)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_BATCH_NUM_ERROR;
            return baseRspParam.toJson();
        }
        if (StringUtils.isEmpty(keywordInfo.asin)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_KEYWORD_ASIN_ERROR;
            return baseRspParam.toJson();
        }
        if (!RegexUtil.isSiteCodeQualified(keywordInfo.siteCode)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR;
            return baseRspParam.toJson();
        }
        if (StringUtils.isEmpty(keywordInfo.keyword)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_KEYWORD_EMPTY;
            return baseRspParam.toJson();
        }
        if (StringUtils.isEmpty(keywordInfo.departmentCode)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DEPARTMENT_CODE_EMPTY;
            return baseRspParam.toJson();
        }

        RankQueryRsp rankQueryRsp = new RankQueryRsp();
        rankQueryRsp.customerCode = baseRspParam.customerCode;
        rankQueryRsp.status = baseRspParam.status;
        rankQueryRsp.msg = baseRspParam.msg;

        try {
            /*判断关键词是否在监听中*/
            CustomerKeywordRank customerKeywordRank = new CustomerKeywordRank();
            customerKeywordRank.customerCode = baseRspParam.customerCode;
            customerKeywordRank.asin = keywordInfo.asin;
            customerKeywordRank.siteCode = keywordInfo.siteCode;
            customerKeywordRank.departmentCode = keywordInfo.departmentCode;
            customerKeywordRank.keyword = keywordInfo.keyword;
            if (!mCustomerKeywordRankService.isExist(customerKeywordRank)) {
                rankQueryRsp.status = R.HttpStatus.PARAM_WRONG;
                rankQueryRsp.msg = R.RequestMsg.PARAMETER_KEYWORD_EMPTY__ERROR;
                return rankQueryRsp.toJson();
            }

            KeywordRank keywordRank = new KeywordRank(keywordInfo.asin, keywordInfo.keyword, keywordInfo.siteCode, keywordInfo.departmentCode);
            KeywordRank kwRank = mKeywordRankService.findByObj(keywordRank);

            if (kwRank == null) {
                rankQueryRsp.msg = R.RequestMsg.QUERY_DATA_EMPTY;
                return rankQueryRsp.toJson();
            }

            RankQueryRsp.KeywordRankInfo keywordRankInfo = rankQueryRsp.new KeywordRankInfo();
            keywordRankInfo.setAsin(kwRank.getAsin());
            keywordRankInfo.setKeyword(kwRank.getKeyword());
            keywordRankInfo.setSiteCode(kwRank.getSiteCode());
            keywordRankInfo.setDepartmentCode(kwRank.getDepartmentCode());
            keywordRankInfo.setBatchNum(rankQueryReq.getData().batchNum);
            keywordRankInfo.setTotalPages(kwRank.getTotalPages());
            keywordRankInfo.setEveryPage(kwRank.getEveryPage());
            keywordRankInfo.setRankNum(kwRank.getRankNum());

            List<RankQueryRsp.KeywordRankInfo.GoodsInfo> goodsInfos = new ArrayList<>();
            RankQueryRsp.KeywordRankInfo.GoodsInfo goodsInfo;
            List<GoodsRankInfo> goodsRankInfos = mGoodsRankInfoService.findByBatchAndKeywordInfo(rankQueryReq.getData().batchNum, keywordInfo.asin, keywordInfo.keyword, keywordInfo.siteCode, keywordInfo.departmentCode);
            for (GoodsRankInfo goodsRankInfo : goodsRankInfos) {
                goodsInfo = keywordRankInfo.new GoodsInfo();
                goodsInfo.setRankNum(goodsRankInfo.getRankNum());
                goodsInfo.setTitle(goodsRankInfo.getTitle());
                goodsInfo.setPrice(goodsRankInfo.getPrice());
                goodsInfo.setPictureUrl(goodsRankInfo.getGoodsPictureUrl());
                goodsInfo.setDeliveryMode(goodsRankInfo.getDeliveryMode());
                goodsInfo.setDistributionMode(goodsRankInfo.getDistributionMode());
                goodsInfo.setDepartment(goodsRankInfo.getDepartmentInfo());
                goodsInfo.setOffersNum(goodsRankInfo.getOffersNum());
                goodsInfo.setGoodsStatus(goodsRankInfo.getGoodsStatus());
                goodsInfos.add(goodsInfo);
            }
            keywordRankInfo.setTop10(goodsInfos);
            rankQueryRsp.setData(keywordRankInfo);
        } catch (Exception e) {
            serverException(rankQueryRsp, e);
        }

        return rankQueryRsp.toJson();
    }

    @Override
    public String getMonitorList(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CusRankRsp cusRankRsp = new CusRankRsp();
        cusRankRsp.customerCode = baseRspParam.customerCode;
        cusRankRsp.status = baseRspParam.status;
        cusRankRsp.msg = baseRspParam.msg;

        try {
            /*查询数据并封闭到返回对象里*/
            List<CusRankRsp.CustomerKeywordRank> customerKeywordRanks = new ArrayList<>();
            CusRankRsp.CustomerKeywordRank customerKeywordRank;

            List<CustomerKeywordRank> ckwRanks = mCustomerKeywordRankService.findByCustomer(baseRspParam.customerCode);
            for (CustomerKeywordRank ckwRank : ckwRanks) {
                customerKeywordRank = cusRankRsp.new CustomerKeywordRank();
                customerKeywordRank.setSiteCode(ckwRank.siteCode);
                customerKeywordRank.setDepartmentCode(ckwRank.departmentCode);
                customerKeywordRank.setKeyword(ckwRank.keyword);
                customerKeywordRank.setAsin(ckwRank.asin);
                customerKeywordRank.setCrawl(ckwRank.crawl);
                customerKeywordRank.setPriority(ckwRank.priority);
                customerKeywordRank.setFrequency(ckwRank.frequency);
                customerKeywordRank.setSyncTime(DateUtils.format(ckwRank.syncTime));
                customerKeywordRank.setCreateTime(DateUtils.format(ckwRank.createTime));
                customerKeywordRank.setUpdateTime(DateUtils.format(ckwRank.updateTime));
                customerKeywordRanks.add(customerKeywordRank);
            }
            cusRankRsp.setData(customerKeywordRanks);
        } catch (Exception e) {
            serverException(cusRankRsp, e);
        }

        return cusRankRsp.toJson();
    }

    /**
     * 校验数据
     */
    private Map<String, String> checkData(List<RankReq.KeywordRank> keywordRanks) {
        Map<String, String> checkResult = new HashMap<>();

        checkResult.put(IS_SUCCESS, "0");
        if (CollectionUtils.isEmpty(keywordRanks)) {
            /*校验请求数据列表是否为空*/
            checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_DATA_NULL_ERROR);
            return checkResult;
        }

        for (RankReq.KeywordRank keywordRank : keywordRanks) {

            if (keywordRank == null) {
                /*校验数据对象是否为空*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR);
                return checkResult;
            }

            if (StringUtils.isEmpty(keywordRank.keyword)) {
                /*校验keyword是不为空*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_KEYWORD_EMPTY);
                return checkResult;
            }

            if (StringUtils.isEmpty(keywordRank.asin)) {
                /*校验keywordRank是否有空的asin码*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_KEYWORD_ASIN_ERROR);
                return checkResult;
            }

            if (!RegexUtil.isSiteCodeQualified(keywordRank.siteCode)) {
                /*校验keywordRank中站点码是否正确（为空或不在存在）*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR);
                return checkResult;
            }

            if (StringUtils.isEmpty(keywordRank.departmentCode)) {
                /*校验keywordRank中品类码是否正确（为空或不在存在）*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_DEPARTMENT_CODE_ERROR);
                return checkResult;
            }

        }

        checkResult.put(IS_SUCCESS, "1");
        checkResult.put(MESSAGE, R.RequestMsg.SUCCESS);
        return checkResult;
    }
}