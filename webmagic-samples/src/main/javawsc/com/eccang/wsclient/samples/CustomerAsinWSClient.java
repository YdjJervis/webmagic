package com.eccang.wsclient.samples;

import com.eccang.pojo.asin.CustomerAsinReq;
import com.eccang.wsclient.customer.CustomerAsinWSService;
import com.google.gson.Gson;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户-ASIN 关系WebService测试样例
 * @date 2016/11/23 17:01
 */
public class CustomerAsinWSClient {

    public static void main(String[] args) {

        CustomerAsinReq asinReq = new CustomerAsinReq();
        asinReq.cutomerCode = "EC_002";
        asinReq.platformCode = "ERP";
        asinReq.token = "123456789";

        CustomerAsinReq.Asin asin = asinReq.new Asin();

//        asin.asin = "B01LXA42XX";
//        asin.siteCode = "US";
//        asin.crawl = "close";
//        asinReq.data.add(null);

        CustomerAsinReq.Asin asin1 = asinReq.new Asin();

        asin1.asin = "B00HYAL84G";
        asin1.siteCode = "US";
        asin1.crawl = 1;
        asinReq.data.add(asin1);

        String result = new CustomerAsinWSService().getCustomerAsinWSPort().setCrawl(new Gson().toJson(asinReq));
        System.out.println(result);
    }
}