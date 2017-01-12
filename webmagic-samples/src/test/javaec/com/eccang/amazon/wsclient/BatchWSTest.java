package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.batch.BatchReq;
import com.eccang.wsclient.batch.BatchWSService;
import com.google.gson.Gson;
import org.junit.Test;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/12 11:16
 */
public class BatchWSTest extends SpringTestCase {

    @Test
    public void getBatchesTest() {
        BatchReq batchReq = new BatchReq();
        batchReq.customerCode = "EC_001";
        batchReq.platformCode = "ERP";
        batchReq.token = "123456789";

        String json = new BatchWSService().getBatchWSPort().getBatches(new Gson().toJson(batchReq));
        System.out.println(json);
    }
}