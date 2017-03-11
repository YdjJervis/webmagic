package com.eccang.amazon.wsclient;

import com.eccang.base.SpringTestCase;
import com.eccang.pojo.followsell.*;
import com.eccang.spider.amazon.pojo.ImportData;
import com.eccang.spider.amazon.service.ImportDataService;
import com.eccang.wsclient.followsell.FollowSellWSService;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/1/7 11:58
 */
public class FollowSellWSTest extends SpringTestCase {

    @Autowired
    private ImportDataService mImportDataService;

    @Test
    public void addToMonitor() {
        List<ImportData> importDataList = mImportDataService.find(null, 6);
//        importDataList = importDataList.subList(5, 6);

        CusFollowSellAddReq req = new CusFollowSellAddReq();
        req.customerCode = "EC_001";
        req.platformCode = "ERP";
        req.token = "123456789";

//        for (ImportData importData : importDataList) {
            CusFollowSellAddReq.FollowSell followSell = req.new FollowSell();
            followSell.siteCode = "US";//importData.getSiteCode();
            followSell.asin = "B01N3UN0QE";//importData.getAsin();
            req.data.add(followSell);
//        }

        String params = new Gson().toJson(req);
        System.out.println(params);

        String result = new FollowSellWSService().getFollowSellWSPort().addToMonitor(params);
        System.out.println(result);
    }

    @Test
    public void getMonitorList() {

        CusFollowSellQueryReq req = new CusFollowSellQueryReq();
        req.customerCode = "EC_001";
        req.platformCode = "ERP";
        req.token = "123456789";

        String params = new Gson().toJson(req);
        System.out.println(params);

        String result = new FollowSellWSService().getFollowSellWSPort().getMonitorList(params);
        System.out.println(result);
    }

    @Test
    public void setStatus() {

        /* 修改 */
        CusFollowSellUpdateReq updateReq = new CusFollowSellUpdateReq();
        updateReq.customerCode = "EC_002";
        updateReq.platformCode = "ERP";
        updateReq.token = "123456789";

        CusFollowSellUpdateReq.FollowSell followSell = updateReq.new FollowSell();
        followSell.siteCode = "UK";
        followSell.asin = "B01LXA42FB";
        followSell.sellerId = "Seller004";
        followSell.crawl = 1;
        updateReq.data.add(followSell);

        String params = new Gson().toJson(updateReq);
        System.out.println(params);

        String result = new FollowSellWSService().getFollowSellWSPort().setStatus(params);
        System.out.println(result);

    }

    @Test
    public void getFollowSellList() {

        FollowSellQueryReq req = new FollowSellQueryReq();
        req.customerCode = "EC_001";
        req.platformCode = "ERP";
        req.token = "123456789";

        FollowSellQueryReq.FollowSell followSell = req.new FollowSell();
        followSell.batchNum = "ECBatch001";
        followSell.siteCode = "US";
        followSell.asin = "Asin001";

        req.data.add(followSell);

        String params = new Gson().toJson(req);
        System.out.println(params);

        String result = new FollowSellWSService().getFollowSellWSPort().getFollowSellList(params);
        System.out.println(result);
    }
}
