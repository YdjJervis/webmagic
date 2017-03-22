package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.asin.AsinReq;
import com.eccang.spider.amazon.pojo.ImportData;
import com.eccang.spider.amazon.service.ImportDataService;
import com.eccang.wsclient.asin.AsinWSService;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @ription:
 * @date 2016/12/7 16:03
 */
public class AsinWSTest extends SpringTestCase {

    @Autowired
    private ImportDataService mImportDataService;

    @Test
    public void testAsinBatchImport() {
        AsinReq asinReq = new AsinReq();
        asinReq.customerCode = "EC_001";
        asinReq.platformCode = "ERP";
        asinReq.token = "123456789";

        AsinReq.Asin asin = asinReq.new Asin();
        asin.asin = "B01N6C6UHA";
        asin.siteCode = "US";
        asin.star = "0-0-1-1-1";
        asinReq.data.add(asin);

        System.out.println(new Gson().toJson(asinReq));
        String json = new AsinWSService().getAsinWSPort().addToCrawl(new Gson().toJson(asinReq));
        System.out.println(json);

    }

    @Test
    public void testAsinBatchImportMany() {
        AsinReq asinReq = new AsinReq();
        asinReq.customerCode = "EC_001";
        asinReq.platformCode = "ERP";
        asinReq.token = "123456789";

        List<ImportData> importDataList = mImportDataService.find(null,20);

        for (ImportData importData : importDataList) {
            AsinReq.Asin asin = asinReq.new Asin();
            asin.asin = importData.getAsin();
            asin.siteCode = importData.getSiteCode();
            asin.star = "0-0-1-1-1";
            asinReq.data.add(asin);
        }

        String param = new Gson().toJson(asinReq);
        System.out.println(param);

        String json = new AsinWSService().getAsinWSPort().addToCrawl(param);
        System.out.println(json);

    }

    @Test
    public void testGetAsinStatus() {
        AsinReq asinReq = new AsinReq();
        asinReq.customerCode = "EC_001";
        asinReq.platformCode = "ERP";
        asinReq.token = "123456789";
        System.out.println(new Gson().toJson(asinReq));
        String json = new AsinWSService().getAsinWSPort().getAsinsStatus(new Gson().toJson(asinReq));
        System.out.println(json);
    }

    @Test
    public void getProductInfo() {
        AsinReq asinReq = new AsinReq();
        asinReq.customerCode = "EC_001";
        asinReq.platformCode = "ERP";
        asinReq.token = "123456789";

        AsinReq.Asin asin = asinReq.new Asin();
        asin.asin = "B01M0MN61Y";
        asin.siteCode = "";
        asinReq.data.add(asin);

        String json = new AsinWSService().getAsinWSPort().getProductInfo(new Gson().toJson(asinReq));
        System.out.println(json);

    }
}