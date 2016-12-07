package us.codecraft.webmagic.amazon.service;

import com.eccang.pojo.AsinReq;
import com.eccang.wsclient.asin.AsinWSService;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.ImportData;
import us.codecraft.webmagic.samples.amazon.service.ImportDataService;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/5 20:18
 */
public class ImportDataServiceTest extends SpringTestCase {
    @Autowired
    ImportDataService mImportDataService;

    @Test
    public void findBySite() {
       String siteCode = "US";
        List<ImportData> importDataList = mImportDataService.findBySite(siteCode);
        System.out.println(importDataList);
    }

    @Test
    public void findAll() {
        List<ImportData> importDataList = mImportDataService.findAll();
        System.out.println(importDataList);
    }

    @Test
    public void find() {
        AsinReq asinReq = new AsinReq();
        asinReq.cutomerCode = "EC_001";
        asinReq.platformCode = "ERP";
        asinReq.token = "123456789";

        List<ImportData> importDataList = mImportDataService.find(null);

        for (ImportData importData : importDataList) {
            AsinReq.Asin asin = asinReq.new Asin();
            asin.asin = importData.getAsin();
            asin.siteCode = importData.getSiteCode();
            asin.priority = 0;
            asin.star = "0-0-1-1-1";
            asinReq.data.add(asin);
        }

        System.out.println(new Gson().toJson(asinReq));
        String json = new AsinWSService().getAsinWSPort().addToCrawl(new Gson().toJson(asinReq));
        System.out.println(json);
    }

}