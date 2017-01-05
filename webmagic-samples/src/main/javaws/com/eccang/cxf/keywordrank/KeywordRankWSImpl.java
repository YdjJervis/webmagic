package com.eccang.cxf.keywordrank;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.*;
import com.eccang.pojo.keywordrank.*;
import com.eccang.spider.amazon.pojo.crawl.GoodsRankInfo;
import com.eccang.spider.amazon.pojo.crawl.KeywordRank;
import com.eccang.spider.amazon.pojo.relation.CustomerKeywordRank;
import com.eccang.spider.amazon.service.crawl.GoodsRankInfoService;
import com.eccang.spider.amazon.service.crawl.KeywordRankService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.eccang.spider.amazon.service.relation.CustomerKeywordRankService;
import com.eccang.util.RegexUtil;
import com.google.gson.Gson;
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
public class KeywordRankWSImpl extends AbstractSpiderWS implements KeywordRankWS {

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

        CustomerKeywordRankReq customerKeywordRankReq;
        try {
            customerKeywordRankReq = new Gson().fromJson(json, CustomerKeywordRankReq.class);
        } catch (Exception e) {
            sLogger.info(e);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        Map<String, String> checkResult = checkData(customerKeywordRankReq.getData());
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        /* 逻辑处理阶段 */
        CustomerKeywordRankRsp customerKeywordRankRsp = new CustomerKeywordRankRsp();
        customerKeywordRankRsp.cutomerCode = customerKeywordRankReq.cutomerCode;
        customerKeywordRankRsp.status = baseRspParam.status;
        customerKeywordRankRsp.msg = baseRspParam.msg;

        try {
            int crawledNum = 0;
            /* 把客户和keywordRank的关系保存起来 */
            List<CustomerKeywordRank> customerKeywordRanks = new ArrayList<>();
            for (CustomerKeywordRankReq.KeywordRank keywordRank : customerKeywordRankReq.getData()) {
                CustomerKeywordRank customerKeywordRank = new CustomerKeywordRank();
                customerKeywordRank.setCustomerCode(customerKeywordRankReq.cutomerCode);
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

            customerKeywordRankRsp.data.totalCount = customerKeywordRanks.size();
            customerKeywordRankRsp.data.newCount = customerKeywordRanks.size() - crawledNum;
            customerKeywordRankRsp.data.oldCount = crawledNum;
        } catch (Exception e) {
            serverException(customerKeywordRankRsp, e);
        }
        /*对应客户下，keywordRank监听业务的使用情况*/
        Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(customerKeywordRankReq.cutomerCode, R.BusinessCode.KEYWORD_RANK_SPIDER);
        customerKeywordRankRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
        customerKeywordRankRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);

        return customerKeywordRankRsp.toJson();
    }

    @Override
    public String setStatus(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CustomerKeywordRankUpdateReq customerKeywordRankUpdateReq;
        try {
            customerKeywordRankUpdateReq = new Gson().fromJson(json, CustomerKeywordRankUpdateReq.class);
        } catch (Exception e) {
            sLogger.info(e);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(customerKeywordRankUpdateReq.getData())) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_NULL_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        Map<String, String> checkResult = checkData(customerKeywordRankUpdateReq.getData());
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        for (CustomerKeywordRankReq.KeywordRank keywordRank : customerKeywordRankUpdateReq.getData()) {

            if (!RegexUtil.isCrawlStatusQualified(keywordRank.getCrawl())) {
                baseRspParam.msg = R.RequestMsg.PARAMETER_STATUS_ERROR;
                return baseRspParam.toJson();
            }
        }

        CustomerKeywordRankUpdateRsp customerKeywordRankUpdateRsp = new CustomerKeywordRankUpdateRsp();
        customerKeywordRankUpdateRsp.cutomerCode = customerKeywordRankUpdateReq.cutomerCode;
        customerKeywordRankUpdateRsp.status = baseRspParam.status;
        customerKeywordRankUpdateRsp.msg = baseRspParam.msg;

        try {
            CustomerKeywordRank customerKeywordRank;
            for (CustomerKeywordRankUpdateReq.KeywordRank keywordRank : customerKeywordRankUpdateReq.getData()) {
                customerKeywordRank = new CustomerKeywordRank();
                customerKeywordRank.setCustomerCode(customerKeywordRankUpdateReq.cutomerCode);
                customerKeywordRank.setSiteCode(keywordRank.getSiteCode());
                customerKeywordRank.setAsin(keywordRank.getAsin());
                customerKeywordRank.setKeyword(keywordRank.getKeyword());
                customerKeywordRank.setDepartmentCode(keywordRank.getDepartmentCode());
                CustomerKeywordRank cKeywordRank = mCustomerKeywordRankService.findByObj(customerKeywordRank);

                if (cKeywordRank == null) {
                    customerKeywordRankUpdateRsp.msg = R.RequestMsg.PARAMETER_KEYWORD_EMPTY__ERROR;
                    return customerKeywordRankUpdateRsp.toJson();
                }

                if (keywordRank.getPriority() == cKeywordRank.getPriority() &&
                        keywordRank.getFrequency() == cKeywordRank.getFrequency() &&
                        keywordRank.getCrawl() == cKeywordRank.getCrawl()) {

                    customerKeywordRankUpdateRsp.data.noChange++;
                } else {
                    cKeywordRank.setPriority(keywordRank.getPriority());
                    cKeywordRank.setFrequency(keywordRank.getFrequency());
                    cKeywordRank.setCrawl(keywordRank.getCrawl());
                    mCustomerKeywordRankService.update(cKeywordRank);
                    customerKeywordRankUpdateRsp.data.changed++;
                }
            }
        } catch (Exception e) {
            serverException(customerKeywordRankUpdateRsp, e);
        }

        /*对应客户下，keywordRank监听业务的使用情况*/
        Map<String, Integer> result = mCustomerBusinessService.getBusinessInfo(customerKeywordRankUpdateReq.cutomerCode, R.BusinessCode.KEYWORD_RANK_SPIDER);
        customerKeywordRankUpdateRsp.data.usableNum = result.get(R.BusinessInfo.USABLE_NUM);
        customerKeywordRankUpdateRsp.data.hasUsedNum = result.get(R.BusinessInfo.HAS_USED_NUM);
        return customerKeywordRankUpdateRsp.toJson();
    }

    @Override
    public String getKeywordRankInfo(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        KeywordRankQueryReq keywordRankQueryReq;
        try {
            keywordRankQueryReq = new Gson().fromJson(json, KeywordRankQueryReq.class);
        } catch (Exception e) {
            sLogger.info(e);
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        KeywordRankQueryReq.KeywordInfo keywordInfo = keywordRankQueryReq.getData();
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

        KeywordRankQueryRsp keywordRankQueryRsp = new KeywordRankQueryRsp();
        keywordRankQueryRsp.cutomerCode = baseRspParam.cutomerCode;
        keywordRankQueryRsp.status = baseRspParam.status;
        keywordRankQueryRsp.msg = baseRspParam.msg;

        try {
            KeywordRank keywordRank = new KeywordRank(keywordInfo.getAsin(), keywordInfo.getKeyword(), keywordInfo.getSiteCode(), keywordInfo.getDepartmentCode());
            KeywordRank kwRank = mKeywordRankService.findByObj(keywordRank);

            if(kwRank == null) {
                keywordRankQueryRsp.msg = R.RequestMsg.QUERY_DATA_EMPTY;
                return keywordRankQueryRsp.toJson();
            }

            KeywordRankQueryRsp.KeywordRankInfo keywordRankInfo = keywordRankQueryRsp.new KeywordRankInfo();
            keywordRankInfo.setAsin(kwRank.getAsin());
            keywordRankInfo.setKeyword(kwRank.getKeyword());
            keywordRankInfo.setSiteCode(kwRank.getSiteCode());
            keywordRankInfo.setDepartmentCode(kwRank.getDepartmentCode());
            keywordRankInfo.setBatchNum(keywordRankQueryReq.getData().getBatchNum());
            keywordRankInfo.setTotalPages(kwRank.getTotalPages());
            keywordRankInfo.setEveryPage(kwRank.getEveryPage());
            keywordRankInfo.setRankNum(kwRank.getRankNum());

            List<KeywordRankQueryRsp.KeywordRankInfo.GoodsInfo> goodsInfos = new ArrayList<>();
            KeywordRankQueryRsp.KeywordRankInfo.GoodsInfo goodsInfo;
            List<GoodsRankInfo> goodsRankInfos = mGoodsRankInfoService.findByBatchAndKeywordInfo(keywordRankQueryReq.getData().getBatchNum(), keywordInfo.getAsin(), keywordInfo.getKeyword(), keywordInfo.getSiteCode(), keywordInfo.getDepartmentCode());
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
            keywordRankQueryRsp.setData(keywordRankInfo);
        } catch (Exception e) {
            serverException(keywordRankQueryRsp, e);
        }

        return keywordRankQueryRsp.toJson();
    }

    /**
     * 校验数据
     */
    private Map<String, String> checkData(List<CustomerKeywordRankReq.KeywordRank> keywordRanks) {
        Map<String, String> checkResult = new HashMap<>();

        checkResult.put(IS_SUCCESS, "0");
        if (CollectionUtils.isEmpty(keywordRanks)) {
            /*校验请求数据列表是否为空*/
            checkResult.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_NULL_ERROR);
            return checkResult;
        }

        for (CustomerKeywordRankReq.KeywordRank keywordRank : keywordRanks) {

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