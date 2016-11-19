package com.eccang.cxf;

import com.eccang.pojo.AsinReq;
import com.eccang.pojo.AsinRsp;
import com.eccang.pojo.BaseRspParam;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.BatchAsin;
import us.codecraft.webmagic.samples.amazon.service.BatchAsinService;
import us.codecraft.webmagic.samples.amazon.service.BatchService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description:
 * @date 2016/11/17 11:51
 */
@WebService
public class AsinWSImpl extends AbstractSpiderWS implements AsinWS {

    @Autowired
    private BatchService mBatchService;

    @Autowired
    private BatchAsinService mBatchAsinService;

    @WebMethod
    public String addToCrawl(String json) throws Exception {

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

        List<Asin> parsedAsinList = new ArrayList<Asin>();

        for (AsinReq.Asin asin : asinReq.data) {
            Asin parsedAsin = new Asin();
            parsedAsin.site.basCode = asin.siteCode;
            parsedAsin.saaAsin = asin.asin;
            parsedAsin.saaStar = asin.star;
            parsedAsin.saaPriority = asin.priority;
            parsedAsin.asinSource.baasCode = asinReq.platformCode;
            parsedAsinList.add(parsedAsin);
        }

        Batch batch = mBatchService.addBatch(asinRsp.cutomerCode, parsedAsinList);

        List<BatchAsin> batchAsinList = mBatchAsinService.findAllByBatchNum(batch.number);

        int newCount = 0;
        for (BatchAsin batchAsin : batchAsinList) {
            if (batchAsin.crawled == 0) {
                newCount++;
            }
        }

        asinRsp.data.number = batch.number;
        asinRsp.data.totalCount = batchAsinList.size();
        asinRsp.data.newCount = newCount;
        asinRsp.data.oldCount = batchAsinList.size() - newCount;

        return asinRsp.toJson();
    }

}