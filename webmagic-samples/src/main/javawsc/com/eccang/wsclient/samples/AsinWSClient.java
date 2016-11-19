package com.eccang.wsclient.samples;

import com.eccang.pojo.AsinReq;
import com.eccang.wsclient.asin.AsinWSService;
import com.google.gson.Gson;

/**
 * @author Jervis
 * @version V0.20
 * @Description: Asin WebService 客户端测试用例
 * @date 2016/11/19 11:32
 */
public class AsinWSClient {

    public static void main(String[] args) throws Exception {
        AsinReq asinReq = new AsinReq();
        AsinReq.Asin asin = asinReq.new Asin();
        asin.asin = "B01CN74MU6";
        asin.siteCode = "CN";
        asin.priority = 1;
        asin.star = "1-1-1-1-1";
//        asinReq.data.add(asin);

        asinReq.cutomerCode = "EC_0012";
        asinReq.platformCode = "system";
        asinReq.token = "123456789";

        String json = new AsinWSService().getAsinWSPort().addToCrawl(new Gson().toJson(asinReq));
        System.out.println(json);
    }

}