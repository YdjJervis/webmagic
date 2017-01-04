package com.eccang.wsclient.samples;

import com.eccang.pojo.business.CustomerBusinessReq;
import com.eccang.wsclient.business.CustomerBusinessWSService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/16 10:33
 */
public class BusinessWSClient {

    public static void main(String[] args) {
        query();
    }

    private static void query() {
        CustomerBusinessReq queryReq = new CustomerBusinessReq();
        queryReq.cutomerCode = "EC_001";
        queryReq.platformCode = "ERP";
        queryReq.token = "123456789";

        List<CustomerBusinessReq.Business> reqData = new ArrayList<CustomerBusinessReq.Business>();
        CustomerBusinessReq.Business business = queryReq.new Business();
        business.setBusinessCode("MS");
        reqData.add(business);
        queryReq.setData(reqData);
        String json = new CustomerBusinessWSService().getCustomerBusinessWSPort().queryBusiness(new Gson().toJson(queryReq));
        System.out.println(json);
    }
}