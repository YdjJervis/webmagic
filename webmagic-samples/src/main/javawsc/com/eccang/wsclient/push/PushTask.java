package com.eccang.wsclient.push;

import com.eccang.spider.amazon.pojo.PushQueue;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.pojo.batch.BatchReview;
import com.eccang.spider.amazon.pojo.dict.API;
import com.eccang.spider.amazon.pojo.relation.CustomerReview;
import com.eccang.spider.amazon.service.PushQueueService;
import com.eccang.spider.amazon.service.batch.BatchAsinService;
import com.eccang.spider.amazon.service.batch.BatchReviewService;
import com.eccang.spider.amazon.service.batch.BatchService;
import com.eccang.spider.amazon.service.dict.APIService;
import com.eccang.spider.amazon.service.relation.CustomerReviewService;
import com.eccang.wsclient.api.Ec_Service;
import com.eccang.wsclient.asin.BaseRspParam;
import com.eccang.wsclient.pojo.PushDataReq;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/5 18:59
 */
@Service
@Transactional
public class PushTask {

    @Autowired
    private PushQueueService mPushQueueService;

    @Autowired
    private BatchService mBatchService;

    @Autowired
    private APIService mAPIService;

    @Autowired
    private BatchAsinService mBatchAsinService;

    @Autowired
    private BatchReviewService mBatchReviewService;

    @Autowired
    private CustomerReviewService mCustomerReviewService;

    private Logger sLogger = Logger.getLogger(getClass());

    void startTask(PushQueue pushQueue) {
        /*更新此推送状态为推送中*/
        pushQueue.status = 1;
        pushQueue.times += 1;
        mPushQueueService.update(pushQueue);
        /*获取这个批次下关联的数据*/
        PushDataReq pushDataReq = getNeedPushData(pushQueue);
        /*推送*/
        boolean isSuccess = true;
        if (pushDataReq.getData().getAsins() != null && pushDataReq.getData().getAsins().size() > 0) {
            isSuccess = push(pushDataReq);
        } else {
            if (!pushDataReq.getData().getType().equalsIgnoreCase("0")) {
                sLogger.info("批次号为(" + pushQueue.batchNum + ")没有数据变化，无需推送数据.");
            } else {
                isSuccess = push(pushDataReq);
            }
        }
        /*判断推送成功或失败*/
        pushQueue.status = isSuccess ? 2 : 3;
        /*更新已经完成的批次表中的状态（推送次数，推送是否完成）*/
        mPushQueueService.update(pushQueue);
    }

    /**
     * 推送批次完成信息
     */
    private boolean push(PushDataReq pushDataReq) {
        boolean pushResult = false;
        try {
//            String response = "";
//            if (pushDataReq.getCustomerCode().equals("EC_001")) {
            /*通过客户码，判断调用推送接口的方式*/
            sLogger.info("开始推送已经完成的批量信息.");
            String response = new Ec_Service(pushDataReq.getWsUrl()).getEcSOAP().pushMessage(pushDataReq.getCustomerCode(), pushDataReq.getPlatformCode(), pushDataReq.getToken(), new Gson().toJson(pushDataReq.getData()));
            sLogger.info("推送响应信息：" + response);
//            } else {
//                sLogger.info("客户（" + pushDataReq.getCustomerCode() + "）" + "没有对接推送接口.");
//            }
            BaseRspParam baseRspParam = new Gson().fromJson(response, BaseRspParam.class);
            if (baseRspParam.getStatus() == 200) {
                pushResult = true;
            }
        } catch (Exception e) {
            sLogger.info(e);
        }
        return pushResult;
    }

    /**
     * 获取队列需要推送的数据
     */
    private PushDataReq getNeedPushData(PushQueue pushQueue) {

        /*查询需要推送的批次信息*/
        Batch batch = mBatchService.findByBatchNumber(pushQueue.batchNum);

        /*查询客户对应的token*/
        API api = mAPIService.findByCode(batch.customerCode);

        PushDataReq pushDataReq = new PushDataReq();
        /*生成公共请求参数*/
        pushDataReq.setCustomerCode(batch.customerCode);
        pushDataReq.setPlatformCode("ERP");
        pushDataReq.setToken(api.token);
        pushDataReq.setWsUrl(api.pushUrl);

        /*查询需要推送的具体数据，并将数据封装在Data对象里*/
        PushDataReq.Data data = getPushData(batch, batch.type);

        /*将查询到的数据封装在请求pushDataReq对象里*/
        pushDataReq.setData(data);

        return pushDataReq;
    }

    /**
     * 通过不同的业务来封装需要推送的数据
     */
    private PushDataReq.Data getPushData(Batch batch, int type) {

        PushDataReq.Data data = new PushDataReq.Data();
        data.setBatchNum(batch.number);
        data.setType(String.valueOf(type));

        if (type == 0) {
            /*全量爬取，只需要返回批次单号*/
            return data;
        } else if (type == 1) {
            /*review监听爬取*/
            data.setAsins(getPushReviewsMonitor(batch));
        } else if (type == 2) {
            /*更新爬取*/
            data.setAsins(getPushAsinsUpdate(batch));
        }
        return data;
    }

    /**
     * 获取更新爬取的变化数据信息
     */
    private List<PushDataReq.Data.Asin> getPushAsinsUpdate(Batch batch) {
        List<PushDataReq.Data.Asin> asins = new ArrayList<>();
        /*查询这个批次下的所有的asin*/
        List<BatchAsin> batchAsinList = mBatchAsinService.findAllByBatchNum(batch.number);
        PushDataReq.Data.Asin asin;
        for (BatchAsin batchAsin : batchAsinList) {
            /*每条asin的大字段信息*/
            String extra = batchAsin.extra;
            if (StringUtils.isNotEmpty(extra)) {
                asin = new PushDataReq.Data.Asin();
                asin.setAsin(batchAsin.asin);
                asin.setSiteCode(batchAsin.siteCode);
                /*将大字段里的信息转换成对象集合*/
                List<PushDataReq.Data.Asin.Review> reviewList = new Gson().fromJson(extra, new TypeToken<List<PushDataReq.Data.Asin.Review>>() {
                }.getType());
                asin.setReviews(reviewList);
                asins.add(asin);
            }
        }
        return asins;
    }

    /**
     * 获取review监听爬取变化的数据
     */
    private List<PushDataReq.Data.Asin> getPushReviewsMonitor(Batch batch) {
        List<PushDataReq.Data.Asin> asins = new ArrayList<PushDataReq.Data.Asin>();
        /*查询当前批次下的所有的监听的review*/
        List<BatchReview> batchReviewList = mBatchReviewService.findAllByBatchNum(batch.number);

        /*监听review中，发生变化的review集合*/
        List<PushDataReq.Data.Asin.Review> reviewList = new ArrayList<>();

        PushDataReq.Data.Asin asin;
        for (BatchReview batchReview : batchReviewList) {
            String extra = batchReview.extra;
            if (StringUtils.isNotEmpty(extra)) {
                /*查询对应客户下的review信息*/
                CustomerReview customerReview = mCustomerReviewService.findCustomerReview(batch.customerCode, batchReview.reviewID);
                String asinStr = customerReview.asin;
                asin = new PushDataReq.Data.Asin();
                asin.setAsin(asinStr);
                asin.setSiteCode(customerReview.siteCode);
                /*将大字段里的信息转换成对象集合*/
                PushDataReq.Data.Asin.Review review = new Gson().fromJson(extra, PushDataReq.Data.Asin.Review.class);
                reviewList.add(review);
                asin.setReviews(reviewList);
                asins.add(asin);
            }
        }
        return asins;
    }

}