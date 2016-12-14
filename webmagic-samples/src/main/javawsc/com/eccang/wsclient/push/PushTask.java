package com.eccang.wsclient.push;

import com.eccang.wsclient.pojo.PushDataReq;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.API;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.PushQueue;
import us.codecraft.webmagic.samples.amazon.service.APIService;
import us.codecraft.webmagic.samples.amazon.service.BatchService;
import us.codecraft.webmagic.samples.amazon.service.PushQueueService;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/5 18:59
 */
@Service
public class PushTask {

    @Autowired
    private PushQueueService mPushQueueService;

    @Autowired
    private BatchService mBatchService;

    @Autowired
    private APIService mAPIService;

    private Logger sLogger = Logger.getLogger(getClass());

    void startTask(PushQueue pushQueue) {
        /*更新此推送状态为推送中*/
        pushQueue.status = 1;
        pushQueue.times += 1;
        mPushQueueService.update(pushQueue);
        /*获取这个批次下关联的数据*/


        /*查询当前批次下的批次状态信息*/
        Batch batch = mBatchService.findByBatchNumber(pushQueue.batchNum);
        /*推送*/
        boolean isSuccess = push(batch);
        /*判断推送成功或失败*/
        pushQueue.status = isSuccess ? 2 : 3;
        /*更新已经完成的批次表中的状态（推送次数，推送是否完成）*/
        mPushQueueService.update(pushQueue);
    }

    /**
     * 推送批次完成信息
     */
    private boolean push(Batch batch) {
        boolean pushResult = false;
        try {
            /*通过客户码，判断调用推送接口的方式*/
            System.out.println("推送已经完成的批量信息.");

            System.out.println(batch);
            pushResult = true;
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
        pushDataReq.setPlatformCode(batch.platformCode);
        pushDataReq.setToken(api.token);

        /*查询需要推送的具体数据，并将数据封装在Data对象里*/
        PushDataReq.Data data = getPushData(batch.customerCode, batch.type);

        /*将查询到的数据封装在请求pushDataReq对象里*/
        pushDataReq.setData(data);

        return pushDataReq;
    }

    /**
     * 通过不同的业务来封闭需要推送的数据
     */
    private PushDataReq.Data getPushData(String batchNum, int type) {

        PushDataReq.Data data = new PushDataReq.Data();
        data.setBatchNum(batchNum);
        data.setType(String.valueOf(type));

        if (type == 0) {
            /*全量爬取，只需要返回批次单号*/
            return data;
        } else if (type == 1) {

        } else if (type == 2) {

        }

        return data;
    }

}