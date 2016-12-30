package us.codecraft.webmagic.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.ImportData;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerFollowSell;
import us.codecraft.webmagic.samples.amazon.service.ImportDataService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerFollowSellService;

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
        customerFollowSell.customerCode = "Customer001";
        customerFollowSell.siteCode = "US";
        customerFollowSell.asin = "Asin001";
        customerFollowSell.frequency = 2;
        mService.add(customerFollowSell);
    }

    @Test
    public void addAll(){
        List<ImportData> importDataList = mImportDataService.find(null);

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
