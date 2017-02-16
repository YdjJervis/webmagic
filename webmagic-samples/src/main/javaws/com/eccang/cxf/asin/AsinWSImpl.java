package com.eccang.cxf.asin;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.asin.*;
import com.eccang.spider.amazon.pojo.Asin;
import com.eccang.spider.amazon.pojo.Business;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.pojo.crawl.Product;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.amazon.pojo.relation.AsinRootAsin;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.amazon.pojo.relation.CustomerPayPackage;
import com.eccang.spider.amazon.service.AsinService;
import com.eccang.spider.amazon.service.BusinessService;
import com.eccang.spider.amazon.service.NoSellService;
import com.eccang.spider.amazon.service.batch.BatchAsinService;
import com.eccang.spider.amazon.service.batch.BatchService;
import com.eccang.spider.amazon.service.crawl.ProductService;
import com.eccang.spider.amazon.service.pay.PayPackageStubService;
import com.eccang.spider.amazon.service.relation.AsinRootAsinService;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.eccang.spider.amazon.service.relation.CustomerPayPackageService;
import com.eccang.spider.amazon.util.DateUtils;
import com.eccang.util.RegexUtil;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private ProductService mProductService;

    @Autowired
    private BusinessService mBusinessService;

    @Autowired
    private CustomerPayPackageService mCustomerPayPackageService;
    @Autowired
    private PayPackageStubService mPayPackageStubService;

    public String addToCrawl(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        AsinReq asinReq = parseRequestParam(json, baseRspParam, AsinReq.class);
        if (asinReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(asinReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;
            return baseRspParam.toJson();
        }

        Map<String, String> checkResult = checkAsinData(asinReq, 1);

        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        try {
            /* 业务及套餐限制验证 */
            Business business = mBusinessService.findByCode(R.BusinessCode.ASIN_SPIDER);
            if (asinReq.data.size() > business.getImportLimit()) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.BUSSINESS_LIMIT;
                return baseRspParam.toJson();
            }

            CustomerBusiness customerBusiness = mCustomerBusinessService.findByCode(asinReq.customerCode, R.BusinessCode.ASIN_SPIDER);
            if (asinReq.data.size() > customerBusiness.maxData - customerBusiness.useData) {
                baseRspParam.status = R.HttpStatus.COUNT_LIMIT;
                baseRspParam.msg = R.RequestMsg.PAY_PACKAGE_LIMIT;
                return baseRspParam.toJson();
            }
        } catch (Exception e) {
            serverException(baseRspParam, e);
            return baseRspParam.toJson();
        }

        /* 逻辑执行阶段 */
        AsinRsp asinRsp = new AsinRsp();
        asinRsp.customerCode = asinReq.customerCode;
        asinRsp.status = baseRspParam.status;
        asinRsp.msg = baseRspParam.msg;

        try {

            /* 从套餐取出该客户该业务的一些默认配置 */
            CustomerPayPackage customerPayPackage = mCustomerPayPackageService.findActived(asinReq.customerCode);
            PayPackageStub payPackageStub = mPayPackageStubService.find(customerPayPackage.packageCode, R.BusinessCode.ASIN_SPIDER);

            /* 转换成批次Asin批次详单表 */
            List<BatchAsin> parsedBatchAsinList = new ArrayList<>();

            /* 分开已经爬取的和没爬取的数量 */
            int crawledNum = 0;
            for (AsinReq.Asin asin : asinReq.data) {

                BatchAsin batchAsin = new BatchAsin();
                batchAsin.siteCode = asin.siteCode.trim();
                batchAsin.asin = asin.asin.trim();
                batchAsin.star = asin.star;
                batchAsin.type = 0;
                batchAsin.status = 0;
                batchAsin.priority = payPackageStub.priority;

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

            Batch batch = mBatchService.addBatch(asinRsp.customerCode, parsedBatchAsinList, 0, 1);

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
            List<CustomerAsin> customerAsinList = new ArrayList<>();
            for (AsinReq.Asin asin : asinReq.data) {
                CustomerAsin customerAsin = new CustomerAsin();
                customerAsin.customerCode = asinReq.customerCode;
                customerAsin.siteCode = asin.siteCode;
                customerAsin.asin = asin.asin;
                customerAsin.star = asin.star;
                customerAsin.frequency = R.AsinSetting.UPDATE_FREQUENCY;
                customerAsin.onSell = mNoSellService.isExist(new Asin(asin.siteCode, asin.asin)) ? 0 : 1;
                customerAsin.priority = payPackageStub.priority;
                customerAsinList.add(customerAsin);
            }
            mCustomerAsinService.addAll(customerAsinList);

            Map<String, Integer> businessInfo = mCustomerBusinessService.getBusinessInfo(asinReq.customerCode, R.BusinessCode.ASIN_SPIDER);
            asinRsp.data.usableNum = businessInfo.get(R.BusinessInfo.USABLE_NUM);
            asinRsp.data.hasUsedNum = businessInfo.get(R.BusinessInfo.HAS_USED_NUM);
        } catch (Exception e) {
            serverException(asinRsp, e);
        }

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
            baseRspParam.msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        Map<String, String> checkResult = checkAsinData(asinReq, 3);
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        /* 逻辑执行阶段 */
        AsinQueryRsp asinQueryRsp = new AsinQueryRsp();
        asinQueryRsp.customerCode = asinQueryReq.customerCode;
        asinQueryRsp.status = baseRspParam.status;
        asinQueryRsp.msg = baseRspParam.msg;

        try {
            CustomerAsin customerAsin;
            for (AsinQueryReq.Asin asin : asinQueryReq.data) {
                customerAsin = new CustomerAsin(baseRspParam.customerCode, asin.siteCode, asin.asin);
                customerAsin = mCustomerAsinService.find(customerAsin);

                if (customerAsin == null) {
                    sLogger.info("客户（" + baseRspParam.customerCode + "）下，不存在asin (" + asin.asin + ").");
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
                    dbAsin.rootAsin = asin.asin;
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
     * 业务导入限制，套餐限制
     * asin导入校验
     * 修改asin优先级
     * asin的查询
     */
    private Map<String, String> checkAsinData(AsinReq asinReq, int type) {
        Map<String, String> result = new HashMap<>();

        for (AsinReq.Asin asin : asinReq.data) {
            result.put(IS_SUCCESS, "0");
            if (asin == null) {
                result.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR);
                return result;
            }

            if (StringUtils.isEmpty(asin.asin)) {
                result.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_LIST_ERROR);
                return result;
            }

            if (!RegexUtil.isSiteCodeQualified(asin.siteCode)) {
                result.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR);
                return result;
            }

            if (type == 1) {
                if (!RegexUtil.isStarRegex(asin.star)) {
                    result.put(MESSAGE, R.RequestMsg.PARAMETER_ASIN_STAR_ERROR);
                    return result;
                }
            }

        }

        result.put(IS_SUCCESS, "1");
        result.put(MESSAGE, R.RequestMsg.SUCCESS);
        return result;
    }

    @Deprecated
    @Override
    public String setPriority(String jsonArray) {
        BaseRspParam baseRspParam = auth(jsonArray);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        AsinReq asinReq = parseRequestParam(jsonArray, baseRspParam, AsinReq.class);
        if (asinReq == null) {
            return baseRspParam.toJson();
        }

        /*校验data数据是不是null*/
        if (CollectionUtils.isEmpty(asinReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        Map<String, String> checkResult = checkAsinData(asinReq, 2);
        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }


        /* 逻辑处理阶段 */
        AsinPriorityRsp priorityRsp = new AsinPriorityRsp();
        priorityRsp.customerCode = baseRspParam.customerCode;
        priorityRsp.status = baseRspParam.status;
        priorityRsp.msg = baseRspParam.msg;

        try {
            /*改变customer-asin关系表中的优先级即可*/
            CustomerAsin customerAsin;
            for (AsinReq.Asin asin : asinReq.data) {
                customerAsin = new CustomerAsin(baseRspParam.customerCode, asin.siteCode, asin.asin);
                customerAsin = mCustomerAsinService.find(customerAsin);
                if (customerAsin != null) {
                    /*if (customerAsin.priority == asin.priority) {
                        priorityRsp.data.noChange++;
                    } else {
                        customerAsin.priority = asin.priority;
                        mCustomerAsinService.update(customerAsin);
                        priorityRsp.data.changed++;
                    }*/
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

        CusAsinsRsp cusAsinsRsp = new CusAsinsRsp();
        cusAsinsRsp.customerCode = baseRspParam.customerCode;
        cusAsinsRsp.status = baseRspParam.status;
        cusAsinsRsp.msg = baseRspParam.msg;

        cusAsinsRsp.data = new ArrayList<>();
        CusAsinsRsp.CustomerAsin cusAsin;
        /*通过客户码查询客户下所有的asin爬取状态*/
        List<CustomerAsin> customerAsins = mCustomerAsinService.findByCustomerCode(baseRspParam.customerCode);

        for (CustomerAsin customerAsin : customerAsins) {
            cusAsin = cusAsinsRsp.new CustomerAsin();
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
            cusAsinsRsp.data.add(cusAsin);
        }
        return cusAsinsRsp.toJson();
    }

    @Override
    public String getProductInfo(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        AsinReq asinReq = parseRequestParam(json, baseRspParam, AsinReq.class);
        if (asinReq == null) {
            return baseRspParam.toJson();
        }

        /* 参数验证阶段 */
        if (CollectionUtils.isEmpty(asinReq.data)) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = R.RequestMsg.PARAMETER_DATA_NULL_ERROR;
            return baseRspParam.toJson();
        }

        String asin;
        String siteCode;
        for (AsinReq.Asin asinObj : asinReq.data) {
            if (asinObj == null) {
                baseRspParam.status = R.HttpStatus.PARAM_WRONG;
                baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ASIN_ERROR;
                return baseRspParam.toJson();
            }
            asin = asinObj.asin;
            siteCode = asinObj.siteCode;

            if (StringUtils.isEmpty(asin)) {
                baseRspParam.status = R.HttpStatus.PARAM_WRONG;
                baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_LIST_ERROR;
                return baseRspParam.toJson();
            }
            if (!RegexUtil.isSiteCodeQualified(siteCode)) {
                baseRspParam.status = R.HttpStatus.PARAM_WRONG;
                baseRspParam.msg = R.RequestMsg.PARAMETER_ASIN_SITECODE_ERROR;
                return baseRspParam.toJson();
            }
        }

        ProductRsp productRsp = new ProductRsp();
        productRsp.customerCode = asinReq.customerCode;
        productRsp.status = baseRspParam.status;
        productRsp.msg = baseRspParam.msg;

        /*返回数据集合*/
        List<ProductRsp.ProductInfo> productInfos = new ArrayList<>();

        /* 查询数据 */
        ProductRsp.ProductInfo productInfo;
        Product product;
        CustomerAsin customerAsin;
        for (AsinReq.Asin asinObj : asinReq.data) {
            /*判断asin在客户下是否存在*/
            customerAsin = new CustomerAsin();
            customerAsin.asin = asinObj.asin;
            customerAsin.customerCode = baseRspParam.customerCode;
            customerAsin.siteCode = asinObj.siteCode;
            if (!mCustomerAsinService.isExist(customerAsin)) {
                sLogger.info("客户" + baseRspParam.customerCode + "下，不存在asin(" + asinObj.asin + ").");
                continue;
            }

            productInfo = productRsp.new ProductInfo();
            /*查询asin对应的rootAsin*/
            AsinRootAsin asinRootAsin = mAsinRootAsinService.findByAsin(asinObj.asin, asinObj.siteCode);

            if (asinRootAsin == null) {
                sLogger.info("asin : " + asinObj.asin + "，asin与rootAsin关系表中不存在");
                continue;
            }

            productInfo.setAsin(asinObj.asin);
            productInfo.setRootAsin(asinRootAsin.rootAsin);

            /*通过rootAsin查询首页信息*/
            product = new Product();
            product.rootAsin = asinRootAsin.rootAsin;
            product.siteCode = asinRootAsin.siteCode;
            product = mProductService.findByObject(product);

            if (product == null) {
                sLogger.info("asin : " + asinObj.asin + "，首页信息不存在.");
                continue;
            }

            productInfo.setSellerId(product.sellerID);
            productInfo.setSellerName(product.sellerName);
            productInfo.setTransId(product.transID);
            productInfo.setTransName(product.transName);
            productInfo.setTitle(product.title);
            productInfo.setPrice(product.price);
            productInfo.setImgUrl(product.imgUrl);
            productInfo.setReviewNum(product.reviewNum);
            productInfo.setReviewStar(product.reviewStar);
            productInfo.setReplyNum(product.replyNum);
            productInfo.setAmazonDelivery(product.amazonDelivery ? 1 : 0);
            productInfo.setFollowSellNum(product.followSellNum);
            productInfo.setAddedTime(product.addedTime);
            productInfo.setCategory(product.category);
            productInfo.setFeature(product.extra);

            productInfos.add(productInfo);
        }
        productRsp.setData(productInfos);

        return productRsp.toJson();
    }
}