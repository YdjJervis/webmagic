package com.eccang.wsclient.samples;

import com.eccang.pojo.CustomerAsinReq;
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
        asinReq.cutomerCode = "EC_001";
        asinReq.platformCode = "ERP";
        asinReq.token = "123456789";

        CustomerAsinReq.Asin asin = asinReq.new Asin();
        asin.asin = "";
        asin.crawl = "close";

        asinReq.data.add(asin);

        String result = new CustomerAsinWSService().getCustomerAsinWSPort().setCrawl(new Gson().toJson(asinReq));
        System.out.println(result);
    }
}