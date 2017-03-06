package com.eccang.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.ImportData;
import com.eccang.spider.amazon.pojo.relation.CustomerFollowSell;
import com.eccang.spider.amazon.service.ImportDataService;
import com.eccang.spider.amazon.service.relation.CustomerFollowSellService;

import java.util.ArrayList;
import java.util.List;

public class CustomerFollowSellServiceTest extends SpringTestCase {

    @Autowired
    private CustomerFollowSellService mService;
    @Autowired
    private ImportDataService mImportDataService;

    @Test
    public void addOne() {
        CustomerFollowSell customerFollowSell = new CustomerFollowSell();
        customerFollowSell.customerCode = "EC_001";
        customerFollowSell.siteCode = "US";
        customerFollowSell.asin = "B00EQBOXHA";
        customerFollowSell.frequency = 2;
        mService.add(customerFollowSell);
    }

    @Test
    public void addAll() {
        List<ImportData> importDataList = mImportDataService.find("US", 100);

        List<CustomerFollowSell> list = new ArrayList<>();
        for (ImportData importData : importDataList) {
            CustomerFollowSell sell = new CustomerFollowSell();
            sell.customerCode = "EC_001";
            sell.siteCode = importData.getSiteCode();
            sell.asin = importData.getAsin();
            sell.frequency = 1;
            list.add(sell);
        }
        mService.addAll(list);
    }
}
