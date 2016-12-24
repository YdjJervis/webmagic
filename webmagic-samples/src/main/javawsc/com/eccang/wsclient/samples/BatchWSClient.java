package com.eccang.wsclient.samples;

import com.eccang.pojo.BatchReq;
import com.eccang.wsclient.batch.BatchWSService;
import com.google.gson.Gson;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次查询Web Service测试用例
 * @date 2016/11/22 15:07
 */
public class BatchWSClient {

    public static void main(String[] args) {

        BatchReq batchReq = new BatchReq();
        batchReq.cutomerCode = "EC_002";
        batchReq.platformCode = "ERP";
        batchReq.token = "123456789";

        batchReq.data = batchReq.new Batch();
        batchReq.data.number = "EC2016122411205651";

        String info = new BatchWSService().getBatchWSPort().getBatchInfo(new Gson().toJson(batchReq));
        System.out.println(info);
    }
}