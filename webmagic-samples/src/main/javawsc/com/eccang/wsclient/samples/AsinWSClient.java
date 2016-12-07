package com.eccang.wsclient.samples;

import com.eccang.pojo.AsinPriorityReq;
import com.eccang.pojo.AsinQueryReq;
import com.eccang.wsclient.asin.AsinWSService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.service.ImportDataService;

/**
 * @author Jervis
 * @version V0.20
 * @Description: Asin WebService 客户端测试用例
 * @date 2016/11/19 11:32
 */
public class AsinWSClient {

    @Autowired
    ImportDataService mImportDataService;

    public static void main(String[] args) throws Exception {
        //add();
//        query();
//        setPriority();
    }

    private static void setPriority() {
        AsinPriorityReq priorityReq = new AsinPriorityReq();
        priorityReq.cutomerCode = "EC_001";
        priorityReq.platformCode = "ERP";
        priorityReq.token = "123456789";

        AsinPriorityReq.Asin asin = priorityReq.new Asin();
        asin.priority = 1;
        asin.asin = "B01M70JMIJ";

        priorityReq.data.add(asin);

        String json = new AsinWSService().getAsinWSPort().setPriority(new Gson().toJson(priorityReq));
        System.out.println(json);
    }

    private static void query() {
        AsinQueryReq queryReq = new AsinQueryReq();
        queryReq.cutomerCode = "EC_001";
        queryReq.platformCode = "ERP";
        queryReq.token = "123456789";

        AsinQueryReq.Asin asin = queryReq.new Asin();
        asin.asin = "B01M6WRC9R1";

        queryReq.data.add(asin);

        String json = new AsinWSService().getAsinWSPort().getAsins(new Gson().toJson(queryReq));
        System.out.println(json);
    }

//    private void add() {
//        AsinReq asinReq = new AsinReq();
//        asinReq.cutomerCode = "EC_001";
//        asinReq.platformCode = "ERP";
//        asinReq.token = "123456789";
//
//        List<ImportData> importDataList = mImportDataService.find(null);
//
//        for (ImportData importData : importDataList) {
//            AsinReq.Asin asin = asinReq.new Asin();
//            asin.asin = importData.getAsin();
//            asin.siteCode = importData.getSiteCode();
//            asin.priority = 0;
//            asin.star = "0-0-1-1-1";
//            asinReq.data.add(asin);
//        }
//
//        System.out.println(new Gson().toJson(asinReq));
//        String json = new AsinWSService().getAsinWSPort().addToCrawl(new Gson().toJson(asinReq));
//        System.out.println(json);
//
//    }

}