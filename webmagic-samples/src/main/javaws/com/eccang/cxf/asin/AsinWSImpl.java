package com.eccang.cxf.asin;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.*;
import com.eccang.pojo.asin.*;
import com.eccang.util.RegexUtil;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.spider.amazon.pojo.*;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.pojo.relation.AsinRootAsin;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.service.*;
import com.eccang.spider.amazon.service.batch.BatchAsinService;
import com.eccang.spider.amazon.service.batch.BatchService;
import com.eccang.spider.amazon.service.relation.AsinRootAsinService;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.eccang.spider.amazon.util.DateUtils;

import javax.jws.WebService;
import java.util.*;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 添加ASIN批次的WebService调用实现
 * @date 2016/11/17 11:51
 */
@WebService
public class AsinWSImpl extends AbstractSpiderWS implements AsinWS {

    @Autowired
    private BatchService mBatchService;

    @Autowired
    private BatchAsinService mBatchAsinService;

    @Autowired
    private AsinService mAsinService;

    @Autowired
    private CustomerAsinService mCustomerAsinService;

    @Autowired
    private NoSellService mNoSellService;

    @Autowired
    private AsinRootAsinService mAsinRootAsinService;

    @Autowired
    private CustomerBusinessService mCustomerBusinessService;

    public String addToCrawl(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        AsinReq asinReq;
        try {
            asinReq = new Gson().fromJson(json, AsinReq.class);
        } catch (Exception e) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(asinReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_NULL_ERROR;
            return baseRspParam.toJson();
        }

        Map<String, String> checkResult = checkAsinData(asinReq.data, 1);

        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        /* 逻辑执行阶段 */
        AsinRsp asinRsp = new AsinRsp();
        asinRsp.cutomerCode = asinReq.cutomerCode;
        asinRsp.status = baseRspParam.status;
        asinRsp.msg = baseRspParam.msg;

        try {
            /* 转换成批次Asin批次详单表 */
            List<BatchAsin> parsedBatchAsinList = new ArrayList<BatchAsin>();

            /* 分开已经爬取的和没爬取的数量 */
            int crawledNum = 0;
            for (AsinReq.Asin asin : asinReq.data) {

                BatchAsin batchAsin = new BatchAsin();
                batchAsin.siteCode = asin.siteCode.trim();
                batchAsin.asin = asin.asin.trim();
                batchAsin.star = asin.star;
                batchAsin.type = 0;
                batchAsin.status = 0;

                AsinRootAsin asinRootAsin = mAsinRootAsinService.findByAsin(asin.asin, asin.siteCode);
                /* 已经爬取过的商品，把爬取完成的大字段同步过来 */
                if (asinRootAsin != null) {
                    crawledNum++;
                    setCrawledStatus(batchAsin);
                    Asin asin1 = mAsinService.findByAsin(asin.siteCode, asinRootAsin.rootAsin);
                    batchAsin.extra = asin1.extra;
                    batchAsin.rootAsin = asinRootAsin.rootAsin;
                }

                /* 已经下架的商品也设置成爬取完毕的状态 */
                if (mNoSellService.isExist(new Asin(asin.siteCode, asin.asin))) {
                    crawledNum++;
                    setCrawledStatus(batchAsin);
                    batchAsin.type = 0;
                }

                parsedBatchAsinList.add(batchAsin);
            }

            Batch batch = mBatchService.addBatch(asinRsp.cutomerCode, parsedBatchAsinList, 0, 1);

            /* 全部都是之前已经爬取过的，就直接更新新批次号各个状态 */
            boolean isAllCrawled = true;
            for (BatchAsin batchAsin : parsedBatchAsinList) {
                if (batchAsin.crawled == 0) {
                    isAllCrawled = false;
                    break;
                }
            }
            if (isAllCrawled) {
                batch.startTime = batch.finishTime = new Date();
                batch.status = 2;
                batch.progress = 1;
                mBatchService.update(batch);
            }

            /* 统计新添加的ASIN的个数 */
            List<BatchAsin> batchAsinList = mBatchAsinService.findAllByBatchNum(batch.number);
            asinRsp.data.number = batch.number;
            asinRsp.data.totalCount = batchAsinList.size();
            asinRsp.data.newCount = batchAsinList.size() - crawledNum;
            asinRsp.data.oldCount = crawledNum;

            /* 把ASIN和客户的关系统计起来 */
            List<CustomerAsin> customerAsinList = new ArrayList<CustomerAsin>();
            for (AsinReq.Asin asin : asinReq.data) {
                CustomerAsin customerAsin = new CustomerAsin();
                customerAsin.customerCode = asinReq.cutomerCode;
                customerAsin.siteCode = asin.siteCode;
                customerAsin.asin = asin.asin;
                customerAsin.star = asin.star;
                customerAsin.frequency = R.AsinSetting.UPDATE_FREQUENCY;
                customerAsin.onSell = mNoSellService.isExist(new Asin(asin.siteCode, asin.asin)) ? 0 : 1;
                customerAsinList.add(customerAsin);
            }
            mCustomerAsinService.addAll(customerAsinList);
        } catch (Exception e) {
            serverException(asinRsp, e);
        }

        Map<String, Integer> businessInfo = mCustomerBusinessService.getBusinessInfo(asinReq.cutomerCode, R.BusinessCode.ASIN_SPIDER);
        asinRsp.data.usableNum = businessInfo.get(R.BusinessInfo.USABLE_NUM);
        asinRsp.data.hasUsedNum = businessInfo.get(R.BusinessInfo.HAS_USED_NUM);

        return asinRsp.toJson();
    }

    /**
     * 获取ASIN的信息
     * 此功能暂时不可用
     */
    @Override
    public String getAsins(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        AsinQueryReq asinQueryReq;
        AsinReq asinReq;
        try {
            asinQueryReq = new Gson().fromJson(json, AsinQueryReq.class);
            asinReq = new Gson().fromJson(json, AsinReq.class);
        } catch (Exception e) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        if (CollectionUtils.isEmpty(asinQueryReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_NULL_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        Map<String, String> checkResult = checkAsinData(asinReq.data, 3);
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        /* 逻辑执行阶段 */
        AsinQueryRsp asinQueryRsp = new AsinQueryRsp();
        asinQueryRsp.cutomerCode = asinQueryReq.cutomerCode;
        asinQueryRsp.status = baseRspParam.status;
        asinQueryRsp.msg = baseRspParam.msg;

        try {
            CustomerAsin customerAsin;
            for (AsinQueryReq.Asin asin : asinQueryReq.data) {
                customerAsin = new CustomerAsin(baseRspParam.cutomerCode, asin.siteCode, asin.asin);
                customerAsin = mCustomerAsinService.find(customerAsin);

                if (customerAsin == null) {
                    sLogger.info("客户（" + baseRspParam.cutomerCode + "）下，不存在asin (" + asin.asin + ").");
                    continue;
                }

                /*查询asin对应的rootAsin*/
                AsinRootAsin asinRootAsin = mAsinRootAsinService.findByAsin(asin.asin, asin.siteCode);
                Asin dbAsin;
                AsinQueryRsp.Asin resultAsin = asinQueryRsp.new Asin();
                if (asinRootAsin != null) {
                    /*查询asin的爬取情况（批次asin表中）*/

                    resultAsin.onSell = 1;
                } else {
                    /*根rootAsin不存在：1.asin下架；2.asin还没有爬取过*/
                    dbAsin = new Asin();
                    dbAsin.siteCode = asin.siteCode;
                    dbAsin.rootAsin = asinRootAsin.rootAsin;
                    if (mNoSellService.isExist(dbAsin)) {
                        resultAsin.onSell = 0;
                    } else {
                        resultAsin.onSell = 1;
                    }
                }
                resultAsin.asin = asin.asin;
                resultAsin.rootAsin = "";
                resultAsin.progress = 0;//有疑问
                asinQueryRsp.data.add(resultAsin);
            }
        } catch (Exception e) {
            serverException(asinQueryRsp, e);
        }

        return asinQueryRsp.toJson();
    }

    /**
     * 设置成已经爬取状态
     */
    private void setCrawledStatus(BatchAsin batchAsin) {
        batchAsin.crawled = 1;
        batchAsin.status = 4;
        batchAsin.progress = 1;
        batchAsin.type = 1;
    }

    /**
     * 校验asin列表中的数据
     * 1:asin导入校验
     * 2：修改asin优先级
     * 3:asin的查询
     */
    private Map<String, String> checkAsinData(List<AsinReq.Asin> asins, int type) {
        Map<String, String> result = new HashMap<>();

        for (AsinReq.Asin asin : asins) {
            result.put(AsinWSImpl.IS_SUCCESS, "0");
            if (asin == null) {
                result.put(AsinWSImpl.MESSAGE, R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR);
                return result;
            }

            if (StringUtils.isEmpty(asin.asin)) {
                result.put(AsinWSImpl.MESSAGE, R.RequestMsg.PARAMETER_ASIN_LIST_ERROR);
                return result;
            }

            if (!RegexUtil.isSiteCodeQualified(asin.siteCode)) {
                result.put(AsinWSImpl.MESSAGE, R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR);
                return result;
            }

            if (type == 1) {
                if (!RegexUtil.isStarRegex(asin.star)) {
                    result.put(AsinWSImpl.MESSAGE, R.RequestMsg.PARAMETER_ASIN_STAR_ERROR);
                    return result;
                }
            }

            if (type == 1 || type == 2) {
                if (!RegexUtil.isPriorityQualified(asin.priority)) {
                    result.put(AsinWSImpl.MESSAGE, R.RequestMsg.PARAMETER_ASIN_PRIORITY_ERROR);
                    return result;
                }
            }
        }

        result.put(AsinWSImpl.IS_SUCCESS, "1");
        result.put(AsinWSImpl.MESSAGE, R.RequestMsg.SUCCESS);
        return result;
    }

    @Override
    public String setPriority(String jsonArray) {
        BaseRspParam baseRspParam = auth(jsonArray);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        AsinPriorityReq priorityReq;
        AsinReq asinReq;
        try {
            priorityReq = new Gson().fromJson(jsonArray, AsinPriorityReq.class);
            asinReq = new Gson().fromJson(jsonArray, AsinReq.class);
        } catch (Exception e) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_FORMAT_ERROR;
            return baseRspParam.toJson();
        }

        /*校验data数据是不是null*/
        if (CollectionUtils.isEmpty(asinReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_NULL_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        Map<String, String> checkResult = checkAsinData(asinReq.data, 2);
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }


        /* 逻辑处理阶段 */
        AsinPriorityRsp priorityRsp = new AsinPriorityRsp();
        priorityRsp.cutomerCode = baseRspParam.cutomerCode;
        priorityRsp.status = baseRspParam.status;
        priorityRsp.msg = baseRspParam.msg;

        try {
            /*改变customer-asin关系表中的优先级即可*/
            CustomerAsin customerAsin;
            for (AsinPriorityReq.Asin asin : priorityReq.data) {
                customerAsin = new CustomerAsin(baseRspParam.cutomerCode, asin.siteCode, asin.asin);
                customerAsin = mCustomerAsinService.find(customerAsin);
                if (customerAsin != null) {
                    if (customerAsin.priority == asin.priority) {
                        priorityRsp.data.noChange++;
                    } else {
                        customerAsin.priority = asin.priority;
                        mCustomerAsinService.update(customerAsin);
                        priorityRsp.data.changed++;
                    }
                } else {
                    /*不存在的asin*/
                    priorityRsp.data.noChange++;
                }
            }
        } catch (Exception e) {
            serverException(priorityRsp, e);
        }

        return priorityRsp.toJson();
    }

    @Override
    public String getAsinsStatus(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        CustomerAsinsRsp customerAsinsRsp = new CustomerAsinsRsp();
        customerAsinsRsp.cutomerCode = baseRspParam.cutomerCode;
        customerAsinsRsp.status = baseRspParam.status;
        customerAsinsRsp.msg = baseRspParam.msg;

        customerAsinsRsp.data = new ArrayList<>();
        CustomerAsinsRsp.CustomerAsin cusAsin;
        /*通过客户码查询客户下所有的asin爬取状态*/
        List<CustomerAsin> customerAsins = mCustomerAsinService.findByCustomerCodeIsOpen(baseRspParam.cutomerCode);

        for (CustomerAsin customerAsin : customerAsins) {
            cusAsin = customerAsinsRsp.new CustomerAsin();
            cusAsin.asin = customerAsin.asin;
            cusAsin.siteCode = customerAsin.siteCode;
            cusAsin.crawl = customerAsin.crawl;
            cusAsin.priority = customerAsin.priority;
            cusAsin.onSell = customerAsin.onSell;
            cusAsin.frequency = customerAsin.frequency;
            cusAsin.star = customerAsin.star;
            cusAsin.syncTime = DateUtils.format(customerAsin.syncTime);
            cusAsin.createTime = DateUtils.format(customerAsin.createTime);
            cusAsin.updateTime = DateUtils.format(customerAsin.updateTime);
            customerAsinsRsp.data.add(cusAsin);
        }
        return customerAsinsRsp.toJson();
    }
}