package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.pay.CusPayAddReq;
import com.eccang.wsclient.pay.PayPackageWSService;
import com.google.gson.Gson;
import org.junit.Test;

/**
 * @author Jervis
 * @version V0.20
 * @ription:
 * @date 2016/12/7 16:03
 */
public class PayPackageWSTest extends SpringTestCase {

    @Test
    public void buy() {
        CusPayAddReq cusPayAddReq = new CusPayAddReq();
        cusPayAddReq.customerCode = "EC_002";
        cusPayAddReq.platformCode = "ERP";
        cusPayAddReq.token = "123456789";

        CusPayAddReq.PayPackageStub stub = cusPayAddReq.new PayPackageStub();
        stub.businessCode = "AS";
        stub.count = 300;
        stub.frequency = 24;
        stub.priority = 4;
        stub.day = 30;
        cusPayAddReq.data.add(stub);

        stub = cusPayAddReq.new PayPackageStub();
        stub.businessCode = "MS";
        stub.count = 200;
        stub.frequency = 24;
        stub.priority = 4;
        stub.day = 30;
        cusPayAddReq.data.add(stub);

        String json = new PayPackageWSService().getPayPackageWSPort().buy(new Gson().toJson(cusPayAddReq));
        System.out.println(json);
    }
}