package com.eccang.wsclient.samples;

import com.eccang.pojo.AsinPriorityReq;
import com.eccang.pojo.AsinQueryReq;
import com.eccang.pojo.AsinReq;
import com.eccang.wsclient.asin.AsinWSService;
import com.google.gson.Gson;

/**
 * @author Jervis
 * @version V0.20
 * @Description: Asin WebService 客户端测试用例
 * @date 2016/11/19 11:32
 */
public class AsinWSClient{

    public static void main(String[] args) throws Exception {
//        add();
//        query();
        setPriority();
    }

    private static void setPriority() {
        AsinPriorityReq priorityReq = new AsinPriorityReq();
        priorityReq.cutomerCode = "EC_001";
        priorityReq.platformCode = "ERP";
        priorityReq.token = "123456789";

        AsinPriorityReq.Asin asin = priorityReq.new Asin();
        asin.priority = 1;
        asin.asin = "B01M70JMIJ";

        priorityReq.data.add(asin);

        String json = new AsinWSService().getAsinWSPort().setPriority(new Gson().toJson(priorityReq));
        System.out.println(json);
    }

    private static void query() {
        AsinQueryReq queryReq = new AsinQueryReq();
        queryReq.cutomerCode = "EC_001";
        queryReq.platformCode = "ERP";
        queryReq.token = "123456789";

        AsinQueryReq.Asin asin = queryReq.new Asin();
        asin.asin = "B01CN74MU6";

        queryReq.data.add(asin);

        String json = new AsinWSService().getAsinWSPort().getAsins(new Gson().toJson(queryReq));
        System.out.println(json);
    }

    private static void add() {
        AsinReq asinReq = new AsinReq();
        AsinReq.Asin asin = asinReq.new Asin();
        asin.asin = "B01CN74MU6";
        asin.siteCode = "CN";
        asin.priority = 1;
        asin.star = "1-1-1-1-1";
//        asinReq.data.add(asin);

        asinReq.cutomerCode = "EC_001";
        asinReq.platformCode = "system";
        asinReq.token = "123456789";

        String json = new AsinWSService().getAsinWSPort().addToCrawl(new Gson().toJson(asinReq));
        System.out.println(json);
    }

}