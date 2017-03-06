package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.pay.CusPayAddReq;
import com.eccang.pojo.pay.CusPayCustomAddReq;
import com.eccang.pojo.pay.PaiedQueryReq;
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
        cusPayCustomAddReq.customerCode = "EC_001";
        cusPayCustomAddReq.platformCode = "ERP";
        cusPayCustomAddReq.token = "123456789";

        CusPayCustomAddReq.PayPackageStub stub = cusPayCustomAddReq.new PayPackageStub();
        stub.businessCode = "AS";
        stub.count = 20;
        stub.frequency = 24;
        stub.priority = 4;
        stub.day = 30;
        cusPayCustomAddReq.data.add(stub);

        stub = cusPayCustomAddReq.new PayPackageStub();
        stub.businessCode = "MS";
        stub.count = 10;
        stub.frequency = 24;
        stub.priority = 4;
        stub.day = 30;
        cusPayCustomAddReq.data.add(stub);

        stub = cusPayCustomAddReq.new PayPackageStub();
        stub.businessCode = "KRS";
        stub.count = 5;
        stub.frequency = 24;
        stub.priority = 4;
        stub.day = 30;
        cusPayCustomAddReq.data.add(stub);

        stub = cusPayCustomAddReq.new PayPackageStub();
        stub.businessCode = "FS";
        stub.count = 5;
        stub.frequency = 12;
        stub.priority = 4;
        stub.day = 30;
        cusPayCustomAddReq.data.add(stub);

        String reqJson = new Gson().toJson(cusPayCustomAddReq);
//        reqJson = "{\"data\":\"\",\"customerCode\":\"EC_001\",\"platformCode\":\"ERP\",\"token\":\"123456789\"}";
        System.out.println(reqJson);

        String json = new PayPackageWSService().getPayPackageWSPort().buyCustom(reqJson);
        System.out.println(json);
    }

    @Test
    public void buy() {
        CusPayAddReq cusPayAddReq = new CusPayAddReq();
        cusPayAddReq.customerCode = "EC_001";
        cusPayAddReq.platformCode = "ERP";
        cusPayAddReq.token = "123456789";

        cusPayAddReq.data.payPackageCode = null;

        String json = new Gson().toJson(cusPayAddReq);
        System.out.println(json);
        json = "{\"data\":\"EC_001\",\"customerCode\":\"EC_001\",\"platformCode\":\"ERP\",\"token\":\"123456789\"}";

        System.out.println(new PayPackageWSService().getPayPackageWSPort().buy(json));
    }

    @Test
    public void getList() {
        PayPackageQueryReq req = new PayPackageQueryReq();
        req.customerCode = "EC_002";
        req.platformCode = "ERP";
        req.token = "123456789";

        String reqJson = new Gson().toJson(req);
        System.out.println(reqJson);
        reqJson = "{\"customerCode\":\"EC_001\",\"platformCode\":\"ERP\",\"token\":\"123456789\"},\"data\":[]";

        System.out.println(new PayPackageWSService().getPayPackageWSPort().getList(reqJson));
    }

    @Test
    public void getPaied() {
        PaiedQueryReq req = new PaiedQueryReq();
        req.customerCode = "EC_002";
        req.platformCode = "ERP";
        req.token = "123456789";

        String reqJson = new Gson().toJson(req);
        System.out.println(reqJson);

        System.out.println(new PayPackageWSService().getPayPackageWSPort().getPaied(reqJson));
    }

}