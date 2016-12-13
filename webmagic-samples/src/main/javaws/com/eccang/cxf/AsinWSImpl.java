package com.eccang.cxf;

import com.eccang.pojo.*;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.*;
import us.codecraft.webmagic.samples.amazon.service.*;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public String addToCrawl(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        AsinReq asinReq = new Gson().fromJson(json, AsinReq.class);
        if (CollectionUtils.isEmpty(asinReq.data)) {
            baseRspParam.status = 413;
            baseRspParam.msg = "Asin列表为空";
            return baseRspParam.toJson();
        }

        AsinRsp asinRsp = new AsinRsp();
        asinRsp.cutomerCode = asinReq.cutomerCode;
        asinRsp.status = baseRspParam.status;
        asinRsp.msg = baseRspParam.msg;

        /* 转换成批次Asin批次详单表 */
        List<BatchAsin> parsedBatchAsinList = new ArrayList<BatchAsin>();

        /* 分开已经爬取的和没爬取的数量 */
        int crawledNum = 0;
        for (AsinReq.Asin asin : asinReq.data) {

            BatchAsin batchAsin = new BatchAsin();
            batchAsin.siteCode = asin.siteCode;
            batchAsin.asin = asin.asin;
            batchAsin.star = asin.star;
            batchAsin.type = 0;
            batchAsin.status = 0;

            AsinRootAsin asinRootAsin = mAsinRootAsinService.findByAsin(asin.asin);
            /* 已经爬取过的商品，把爬取完成的大字段同步过来 */
            if (asinRootAsin != null) {
                crawledNum++;
                setCrawledStatus(batchAsin);
                batchAsin.extra = mAsinService.findByAsin(asin.siteCode, asinRootAsin.rootAsin).extra;
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

        Batch batch = mBatchService.addBatch(asinRsp.cutomerCode, parsedBatchAsinList, 0);

        /* 全部都是之前已经爬取过的，就直接跟新批次号各个状态 */
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
            customerAsinList.add(customerAsin);
        }
        mCustomerAsinService.addAll(customerAsinList);

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

    @Override
    public String getAsins(String json) {
        BaseRspParam baseRspParam = auth(json);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        AsinQueryReq asinQueryReq = new Gson().fromJson(json, AsinQueryReq.class);
        if (CollectionUtils.isEmpty(asinQueryReq.data)) {
            baseRspParam.status = 413;
            baseRspParam.msg = "Asin查询列表为空";
            return baseRspParam.toJson();
        }

        AsinQueryRsp asinQueryRsp = new AsinQueryRsp();
        asinQueryRsp.cutomerCode = asinQueryReq.cutomerCode;
        asinQueryRsp.status = baseRspParam.status;
        asinQueryRsp.msg = baseRspParam.msg;

        CustomerAsin customerAsin;
        for (AsinQueryReq.Asin asin : asinQueryReq.data) {
            /*查询客户-ASIN关系表中的数据*/
            customerAsin = new CustomerAsin(asinQueryReq.cutomerCode, asin.siteCode, asin.asin);
            CustomerAsin customerAsin1 = mCustomerAsinService.find(customerAsin);
            /*查询asin对应的rootAsin*/
            AsinRootAsin asinRootAsin = mAsinRootAsinService.findByAsin(asin.asin);
            Asin dbAsin;
            AsinQueryRsp.Asin resultAsin = asinQueryRsp.new Asin();
            if (asinRootAsin != null) {
                /*通过rootAsin与siteCode查询基础数据*/
                dbAsin = mAsinService.findByAsin(asin.siteCode, asinRootAsin.rootAsin);
                resultAsin.onSale = 1;
            } else {
                /*根rootAsin不存在：1.asin下架；2.asin还没有爬取过*/
                dbAsin = new Asin();
                dbAsin.siteCode = asin.siteCode;
                dbAsin.rootAsin = asin.asin;
                if (mNoSellService.isExist(dbAsin)) {
                    resultAsin.onSale = 0;
                } else {
                    resultAsin.onSale = 1;
                }
            }
            resultAsin.asin = dbAsin.rootAsin;
            resultAsin.rootAsin = dbAsin.rootAsin;
//            resultAsin.progress = customerAsin1.progress;
            asinQueryRsp.data.add(resultAsin);
        }

        return asinQueryRsp.toJson();
    }

    @Override
    public String setPriority(String jsonArray) {
        BaseRspParam baseRspParam = auth(jsonArray);

        if (!baseRspParam.isSuccess()) {
            return baseRspParam.toJson();
        }

        AsinPriorityReq priorityReq = new Gson().fromJson(jsonArray, AsinPriorityReq.class);

        AsinPriorityRsp priorityRsp = new AsinPriorityRsp();
        priorityRsp.cutomerCode = baseRspParam.cutomerCode;
        priorityRsp.status = baseRspParam.status;
        priorityRsp.msg = baseRspParam.msg;

        /*改变customer-asin关系表中的优先级即可*/
        CustomerAsin customerAsin;
        for (AsinPriorityReq.Asin asin : priorityReq.data) {
            customerAsin = new CustomerAsin(baseRspParam.cutomerCode, asin.siteCode, asin.asin);
            customerAsin = mCustomerAsinService.find(customerAsin);
            if (customerAsin.priority == asin.priority) {
                priorityRsp.data.noChange++;
            } else {
                customerAsin.priority = asin.priority;
                mCustomerAsinService.update(customerAsin);
                priorityRsp.data.changed++;
            }
        }

        return priorityRsp.toJson();
    }
}