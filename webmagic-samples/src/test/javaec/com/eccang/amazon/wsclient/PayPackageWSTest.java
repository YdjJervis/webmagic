package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.pay.CusPayAddReq;
import com.eccang.pojo.pay.CusPayCustomAddReq;
import com.eccang.pojo.pay.PayPackageQueryReq;
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
    public void buyCustom() {
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

        String json = new PayPackageWSService().getPayPackageWSPort().buyCustom(new Gson().toJson(cusPayCustomAddReq));
        System.out.println(json);
    }

    @Test
    public void buy() {
        CusPayAddReq cusPayAddReq = new CusPayAddReq();
        cusPayAddReq.customerCode = "EC_001";
        cusPayAddReq.platformCode = "ERP";
        cusPayAddReq.token = "123456789";

        cusPayAddReq.data.payPackageCode = "f1b014";
        System.out.println(new PayPackageWSService().getPayPackageWSPort().buy(new Gson().toJson(cusPayAddReq)));
    }

    @Test
    public void getList(){
        PayPackageQueryReq req = new PayPackageQueryReq();
        req.customerCode = "EC_001";
        req.platformCode = "ERP";
        req.token = "123456789";

        System.out.println(new PayPackageWSService().getPayPackageWSPort().getList(new Gson().toJson(req)));
    }

    @Test
    public void getPaied(){

    }

}