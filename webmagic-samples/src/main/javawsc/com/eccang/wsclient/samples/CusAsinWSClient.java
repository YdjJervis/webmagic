package com.eccang.wsclient.samples;

import com.eccang.pojo.asin.CusAsinReq;
import com.eccang.wsclient.customer.CustomerAsinWSService;
import com.google.gson.Gson;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户-ASIN 关系WebService测试样例
 * @date 2016/11/23 17:01
 */
public class CusAsinWSClient {

    public static void main(String[] args) {

        CusAsinReq asinReq = new CusAsinReq();
        asinReq.customerCode = "EC_002";
        asinReq.platformCode = "ERP";
        asinReq.token = "123456789";

        CusAsinReq.Asin asin = asinReq.new Asin();

//        asin.asin = "B01LXA42XX";
//        asin.siteCode = "US";
//        asin.crawl = "close";
//        asinReq.data.add(null);

        CusAsinReq.Asin asin1 = asinReq.new Asin();

        asin1.asin = "B01LXA42FB";
        asin1.siteCode = "UK";
        asin1.crawl = 1;
        asinReq.data.add(asin1);

        asin1 = asinReq.new Asin();
        asin1.asin = "B01FPAJ2A8";
        asin1.siteCode = "DE";
        asin1.crawl = 1;
        asinReq.data.add(asin1);

        asin1 = asinReq.new Asin();
        asin1.asin = "B01H89KA6Y";
        asin1.siteCode = "FR";
        asin1.crawl = 1;
        asinReq.data.add(asin1);

        String result = new CustomerAsinWSService().getCustomerAsinWSPort().setCrawl(new Gson().toJson(asinReq));
        System.out.println(result);
    }
}