package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.pay.CusPayCustomAddReq;
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
        CusPayCustomAddReq cusPayCustomAddReq = new CusPayCustomAddReq();
        cusPayCustomAddReq.customerCode = "EC_002";
        cusPayCustomAddReq.platformCode = "ERP";
        cusPayCustomAddReq.token = "123456789";

        CusPayCustomAddReq.PayPackageStub stub = cusPayCustomAddReq.new PayPackageStub();
        stub.businessCode = "AS";
        stub.count = 300;
        stub.frequency = 24;
        stub.priority = 4;
        stub.day = 30;
        cusPayCustomAddReq.data.add(stub);

        stub = cusPayCustomAddReq.new PayPackageStub();
        stub.businessCode = "MS";
        stub.count = 200;
        stub.frequency = 24;
        stub.priority = 4;
        stub.day = 30;
        cusPayCustomAddReq.data.add(stub);

        String json = new PayPackageWSService().getPayPackageWSPort().buy(new Gson().toJson(cusPayCustomAddReq));
        System.out.println(json);
    }
}