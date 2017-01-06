package com.eccang.cxf.rank;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.rank.*;
import com.eccang.spider.amazon.pojo.crawl.GoodsRankInfo;
import com.eccang.spider.amazon.pojo.crawl.KeywordRank;
import com.eccang.spider.amazon.pojo.relation.CustomerKeywordRank;
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

    @Override
    public String addToMonitor(String json) {
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

        /* 逻辑处理阶段 */
        RankRsp rankRsp = new RankRsp();
        rankRsp.customerCode = rankReq.customerCode;
        rankRsp.status = baseRspParam.status;
        rankRsp.msg = baseRspParam.msg;

        try {
            int crawledNum = 0;
            /* 把客户和keywordRank的关系保存起来 */
            List<CustomerKeywordRank> customerKeywordRanks = new ArrayList<>();
            for (RankReq.KeywordRank keywordRank : rankReq.getData()) {
                CustomerKeywordRank customerKeywordRank = new CustomerKeywordRank();
                customerKeywordRank.setCustomerCode(rankReq.customerCode);
                customerKeywordRank.setSiteCode(keywordRank.getSiteCode());
                customerKeywordRank.setAsin(keywordRank.getAsin());
                customerKeywordRank.setKeyword(keywordRank.getKeyword());
                customerKeywordRank.setDepartmentCode(keywordRank.getDepartmentCode());
                customerKeywordRank.setPriority(keywordRank.getPriority());
                customerKeywordRank.setFrequency(keywordRank.getFrequency());
                if (mCustomerKeywordRankService.isExist(customerKeywordRank)) {
                    crawledNum++;
                }
                customerKeywordRanks.add(customerKeywordRank);
            }
            mCustomerKeywordRankService.addAll(customerKeywordRanks);

            rankRsp.data.totalCount = customerKeywordRanks.size();
            rankRsp.data.newCount = customerKeywordRanks.size() - crawledNum;
            rankRsp.data.oldCount = crawledNum;
        } catch (Exception e) {
            serverException(rankRsp, e);
        }
        /*对应客户下，keywordRank监听业务的使用情况*/
        Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(rankReq.customerCode, R.BusinessCode.KEYWORD_RANK_SPIDER);
        rankRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
        rankRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);

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
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_NULL_ERROR;
            return baseRspParam.toJson();
        }

        Map<String, String> checkResult = checkData(cusRankUpdateReq.getData());
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        for (RankReq.KeywordRank keywordRank : cusRankUpdateReq.getData()) {

            if (!RegexUtil.isCrawlStatusQualified(keywordRank.getCrawl())) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_STATUS_ERROR;
                return baseRspParam.toJson();
            }
        }

        CusRankUpdateRsp cusRankUpdateRsp = new CusRankUpdateRsp();
        cusRankUpdateRsp.customerCode = cusRankUpdateReq.customerCode;
        cusRankUpdateRsp.status = baseRspParam.status;
        cusRankUpdateRsp.msg = baseRspParam.msg;

        try {
            CustomerKeywordRank customerKeywordRank;
            for (RankReq.KeywordRank keywordRank : cusRankUpdateReq.getData()) {
                customerKeywordRank = new CustomerKeywordRank();
                customerKeywordRank.setCustomerCode(cusRankUpdateReq.customerCode);
                customerKeywordRank.setSiteCode(keywordRank.getSiteCode());
                customerKeywordRank.setAsin(keywordRank.getAsin());
                customerKeywordRank.setKeyword(keywordRank.getKeyword());
                customerKeywordRank.setDepartmentCode(keywordRank.getDepartmentCode());
                CustomerKeywordRank cKeywordRank = mCustomerKeywordRankService.findByObj(customerKeywordRank);

                if (cKeywordRank == null) {
                    cusRankUpdateRsp.msg = R.RequestMsg.PARAMETER_KEYWORD_EMPTY__ERROR;
                    return cusRankUpdateRsp.toJson();
                }

                if (keywordRank.getPriority() == cKeywordRank.getPriority() &&
                        keywordRank.getFrequency() == cKeywordRank.getFrequency() &&
                        keywordRank.getCrawl() == cKeywordRank.getCrawl()) {

                    cusRankUpdateRsp.data.noChange++;
                } else {
                    cKeywordRank.setPriority(keywordRank.getPriority());
                    cKeywordRank.setFrequency(keywordRank.getFrequency());
                    cKeywordRank.setCrawl(keywordRank.getCrawl());
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
        RankQueryReq.KeywordInfo keywordInfo = rankQueryReq.getData();
        if (StringUtils.isEmpty(keywordInfo.getBatchNum())) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_BATCH_NUM_ERROR;
            return baseRspParam.toJson();
        }
        if (StringUtils.isEmpty(keywordInfo.getAsin())) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_EMPTY;
            return baseRspParam.toJson();
        }
        if (RegexUtil.isSiteCodeQualified(keywordInfo.getSiteCode())) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR;
            return baseRspParam.toJson();
        }
        if (StringUtils.isEmpty(keywordInfo.getKeyword())) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_KEYWORD_EMPTY;
            return baseRspParam.toJson();
        }
        if (StringUtils.isEmpty(keywordInfo.getDepartmentCode())) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DEPARTMENT_CODE_EMPTY;
            return baseRspParam.toJson();
        }

        RankQueryRsp rankQueryRsp = new RankQueryRsp();
        rankQueryRsp.customerCode = baseRspParam.customerCode;
        rankQueryRsp.status = baseRspParam.status;
        rankQueryRsp.msg = baseRspParam.msg;

        try {
            KeywordRank keywordRank = new KeywordRank(keywordInfo.getAsin(), keywordInfo.getKeyword(), keywordInfo.getSiteCode(), keywordInfo.getDepartmentCode());
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
            keywordRankInfo.setBatchNum(rankQueryReq.getData().getBatchNum());
            keywordRankInfo.setTotalPages(kwRank.getTotalPages());
            keywordRankInfo.setEveryPage(kwRank.getEveryPage());
            keywordRankInfo.setRankNum(kwRank.getRankNum());

            List<RankQueryRsp.KeywordRankInfo.GoodsInfo> goodsInfos = new ArrayList<>();
            RankQueryRsp.KeywordRankInfo.GoodsInfo goodsInfo;
            List<GoodsRankInfo> goodsRankInfos = mGoodsRankInfoService.findByBatchAndKeywordInfo(rankQueryReq.getData().getBatchNum(), keywordInfo.getAsin(), keywordInfo.getKeyword(), keywordInfo.getSiteCode(), keywordInfo.getDepartmentCode());
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

        CusRankReq cusRankReq = parseRequestParam(json, baseRspParam, CusRankReq.class);
        if (cusRankReq == null) {
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
                customerKeywordRank.setSiteCode(ckwRank.getSiteCode());
                customerKeywordRank.setDepartmentCode(ckwRank.getDepartmentCode());
                customerKeywordRank.setKeyword(ckwRank.getKeyword());
                customerKeywordRank.setAsin(ckwRank.getAsin());
                customerKeywordRank.setCrawl(ckwRank.getCrawl());
                customerKeywordRank.setPriority(ckwRank.getPriority());
                customerKeywordRank.setFrequency(ckwRank.getFrequency());
                customerKeywordRank.setSyncTime(DateUtils.format(ckwRank.getSyncTime()));
                customerKeywordRank.setCreateTime(DateUtils.format(ckwRank.getCreateTime()));
                customerKeywordRank.setUpdateTime(DateUtils.format(ckwRank.getUpdateTime()));
                customerKeywordRanks.add(customerKeywordRank);
            }
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
            checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_NULL_ERROR);
            return checkResult;
        }

        for (RankReq.KeywordRank keywordRank : keywordRanks) {

            if (keywordRank == null) {
                /*校验数据对象是否为空*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR);
                return checkResult;
            }

            if (StringUtils.isEmpty(keywordRank.getKeyword())) {
                /*校验keyword是不为空*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_KEYWORD_EMPTY);
                return checkResult;
            }

            if (StringUtils.isEmpty(keywordRank.getAsin())) {
                /*校验keywordRank是否有空的asin码*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_KEYWORD_ASIN_ERROR);
                return checkResult;
            }

            if (!RegexUtil.isSiteCodeQualified(keywordRank.getSiteCode())) {
                /*校验keywordRank中站点码是否正确（为空或不在存在）*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR);
                return checkResult;
            }

            if (StringUtils.isEmpty(keywordRank.getDepartmentCode())) {
                /*校验keywordRank中品类码是否正确（为空或不在存在）*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_DEPARTMENT_CODE_ERROR);
                return checkResult;
            }

            if (!RegexUtil.isFrequencyQualified(keywordRank.getFrequency())) {
                /*校验爬取频率是否在取值范围内*/
                checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_REVIEW_FREQUENCY_ERROR);
                return checkResult;
            }

            if (!RegexUtil.isPriorityQualified(keywordRank.getPriority())) {
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