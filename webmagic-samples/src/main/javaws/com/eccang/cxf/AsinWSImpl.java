package com.eccang.cxf;

import com.eccang.pojo.*;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.BatchAsin;
import us.codecraft.webmagic.samples.amazon.service.AsinService;
import us.codecraft.webmagic.samples.amazon.service.BatchAsinService;
import us.codecraft.webmagic.samples.amazon.service.BatchService;
import us.codecraft.webmagic.samples.amazon.service.UrlService;

import javax.jws.WebService;
import java.util.ArrayList;
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
    RelationCustomerAsinService mRelationCustomerAsinService;

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
        for (AsinReq.Asin asin : asinReq.data) {
            BatchAsin batchAsin = new BatchAsin();
            batchAsin.siteCode = asin.siteCode;
            batchAsin.asin = asin.asin;
            batchAsin.star = asin.star;
            parsedBatchAsinList.add(batchAsin);
        }
        Batch batch = mBatchService.addBatch(asinRsp.cutomerCode, parsedBatchAsinList);

        /* 统计新添加的ASIN的个数 */
        List<BatchAsin> batchAsinList = mBatchAsinService.findAllByBatchNum(batch.number);
        asinRsp.data.number = batch.number;
        asinRsp.data.totalCount = batchAsinList.size();
        asinRsp.data.newCount = batchAsinList.size();
        asinRsp.data.oldCount = 0;

        /* 把ASIN和客户的关系统计起来 */
        List<CustomerAsin> customerAsinList = new ArrayList<CustomerAsin>();
        for (AsinReq.Asin asin : asinReq.data) {
            CustomerAsin customerAsin = new CustomerAsin();
            customerAsin.customerCode = asinReq.cutomerCode;
            customerAsin.siteCode = asin.siteCode;
            customerAsin.asin = asin.asin;
            customerAsinList.add(customerAsin);
        }
        mCustomerAsinService.addAll(customerAsinList);

        return asinRsp.toJson();
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

        for (AsinQueryReq.Asin asin : asinQueryReq.data) {

            /*查询对应客户下asin是否存在，不存在则查询下一个asin*/
            if (!mRelationCustomerAsinService.isExisted(asinQueryReq.cutomerCode ,asin.asin)) {
                continue;
            }
            Asin dbAsin = mAsinService.findByAsin(asin.siteCode, asin.asin);
            AsinQueryRsp.Asin resultAsin = asinQueryRsp.new Asin();
            if (dbAsin == null) {
                continue;
            }
            resultAsin.asin = dbAsin.asin;
            resultAsin.onSale = dbAsin.onSale;
            resultAsin.progress = dbAsin.progress;
            resultAsin.asin = dbAsin.rootAsin;
            resultAsin.rootAsin = dbAsin.rootAsin;
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

        /* 改变ASIN表对应记录的优先级 & 改变URL表对应ASIN的优先级 */
        for (AsinPriorityReq.Asin asin : priorityReq.data) {
            Asin dbAsin = mAsinService.findByAsin(asin.siteCode, asin.asin);
            if (dbAsin != null) {
                /*if (asin.priority > dbAsin.saaPriority) {
                    priorityRsp.data.changed++;
                    dbAsin.saaPriority = asin.priority;
                    mUrlService.updatePriority(dbAsin.asin, dbAsin.saaPriority);
                    mAsinService.update(dbAsin);
                } else {
                    priorityRsp.data.noChange++;
                }*/
            } else {
                priorityRsp.data.noChange++;
            }
        }

        return priorityRsp.toJson();
    }
}