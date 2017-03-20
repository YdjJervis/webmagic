package com.eccang.cxf.asin;

import com.eccang.R;
import com.eccang.cxf.AbstractSpiderWS;
import com.eccang.pojo.BaseRspParam;
import com.eccang.pojo.asin.AsinReq;
import com.eccang.pojo.asin.AsinRsp;
import com.eccang.pojo.asin.CusAsinsRsp;
import com.eccang.pojo.asin.ProductRsp;
import com.eccang.spider.amazon.pojo.Asin;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.pojo.crawl.Product;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.amazon.pojo.relation.AsinRootAsin;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.amazon.pojo.relation.CustomerPayPackage;
import com.eccang.spider.amazon.service.AsinService;
import com.eccang.spider.amazon.service.NoSellService;
import com.eccang.spider.amazon.service.PushQueueService;
import com.eccang.spider.amazon.service.batch.BatchAsinService;
import com.eccang.spider.amazon.service.batch.BatchService;
import com.eccang.spider.amazon.service.crawl.ProductService;
import com.eccang.spider.amazon.service.relation.AsinRootAsinService;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;
import com.eccang.spider.amazon.service.relation.CustomerBusinessService;
import com.eccang.spider.amazon.util.DateUtils;
import com.eccang.util.RegexUtil;
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
    private PushQueueService mPushQueueService;

    @Override
    public String addToCrawl(String json) {
        return dispatchAddToCrawl(json, false);
    }

    @Override
    public String addToCrawlImmediate(String json) {
        return dispatchAddToCrawl(json, true);
    }

    private String dispatchAddToCrawl(String json, boolean immediate) {
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

        /* 数据去重 - start */
        Map<String, AsinReq.Asin> asinMap = new HashMap<>();
        for (AsinReq.Asin asin : asinReq.data) {
            asinMap.put(asin.siteCode + asin.asin, asin);
        }
        asinReq.data.clear();
        for (AsinReq.Asin asin : asinMap.values()) {
            asinReq.data.add(asin);
        }
        /* 数据去重 - end */

        Map<String, String> checkResult = checkAsinData(asinReq, 1);

        if (checkResult.get(IS_SUCCESS).equalsIgnoreCase("0")) {
            baseRspParam.status = R.HttpStatus.PARAM_WRONG;
            baseRspParam.msg = checkResult.get(MESSAGE);
            return baseRspParam.toJson();
        }

        /* 存储当前业务码 */
        String businessCode = immediate ? R.BusinessCode.IMMEDIATE_ASIN_SPIDER : R.BusinessCode.ASIN_SPIDER;

        /* 业务，套餐验证 */
        CustomerBusiness customerBusiness = getCusBusAndValidate(baseRspParam, businessCode, asinReq.data.size());
        if (!baseRspParam.isSuccess()) {
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
            PayPackageStub payPackageStub = mPayPackageStubService.find(customerPayPackage.packageCode, businessCode);

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

                if (!immediate) {
                    AsinRootAsin asinRootAsin = mAsinRootAsinService.findByAsin(asin.asin, asin.siteCode);
                    /* 已经爬取过的商品，把爬取完成的大字段同步过来 */
                    if (asinRootAsin != null) {
                        crawledNum++;
                        setCrawledStatus(batchAsin);
                        Asin dbAsin = mAsinService.findByAsin(asin.siteCode, asinRootAsin.rootAsin);
                        batchAsin.extra = dbAsin.extra;
                        batchAsin.rootAsin = asinRootAsin.rootAsin;
                    }

                    /* 已经下架的商品也设置成爬取完毕的状态 */
                    if (mNoSellService.isExist(new Asin(asin.siteCode, asin.asin))) {
                        crawledNum++;
                        setCrawledStatus(batchAsin);
                        batchAsin.type = 0;
                    }
                }

                parsedBatchAsinList.add(batchAsin);
            }

            Batch batch = mBatchService.addBatch(asinRsp.customerCode, parsedBatchAsinList, 0, 1, immediate ? 1 : 0);

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

                /* 导入相同批次，直接数据推送 */
                mPushQueueService.add(batch.number);
            }

            /* 统计新添加的ASIN的个数 */
            List<BatchAsin> batchAsinList = mBatchAsinService.findAllByBatchNum(batch.number);
            asinRsp.data.number = batch.number;
            asinRsp.data.totalCount = batchAsinList.size();
            asinRsp.data.newCount = immediate ? 0 : batchAsinList.size() - crawledNum;
            asinRsp.data.oldCount = immediate ? 0 : crawledNum;

            /* 把ASIN和客户的关系统计起来 */
            List<CustomerAsin> customerAsinList = new ArrayList<>();
            for (AsinReq.Asin asin : asinReq.data) {
                CustomerAsin customerAsin = new CustomerAsin();
                customerAsin.customerCode = asinReq.customerCode;
                customerAsin.siteCode = asin.siteCode;
                customerAsin.immediate = immediate ? 1 : 0;
                customerAsin.asin = asin.asin;
                customerAsin.star = asin.star;
                customerAsin.frequency = R.AsinSetting.UPDATE_FREQUENCY;
                customerAsin.onSell = mNoSellService.isExist(new Asin(asin.siteCode, asin.asin)) ? 0 : 1;
                customerAsin.priority = payPackageStub.priority;
                customerAsinList.add(customerAsin);
            }
            mCustomerAsinService.addAll(customerAsinList);

            if (immediate) {
                asinRsp.data.hasUsedNum = customerBusiness.useData + customerAsinList.size();
                asinRsp.data.usableNum = customerBusiness.maxData - asinRsp.data.hasUsedNum;

                /* 更新业务表 */
                customerBusiness.useData = asinRsp.data.hasUsedNum;
                mCustomerBusinessService.update(customerBusiness);
            } else {
                Map<String, Integer> businessInfo = mCustomerBusinessService.getBusinessInfo(asinReq.customerCode, businessCode);
                asinRsp.data.hasUsedNum = businessInfo.get(R.BusinessInfo.HAS_USED_NUM);
                asinRsp.data.usableNum = businessInfo.get(R.BusinessInfo.USABLE_NUM);
            }
        } catch (Exception e) {
            serverException(asinRsp, e);
        }

        return asinRsp.toJson();
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
     * asin导入校验
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