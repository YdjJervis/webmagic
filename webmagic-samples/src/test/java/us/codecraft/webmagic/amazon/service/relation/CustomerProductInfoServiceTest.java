package us.codecraft.webmagic.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerProductInfo;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerProductInfoService;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2016/12/26 11:47
 */
public class CustomerProductInfoServiceTest extends SpringTestCase {

    @Autowired
    private CustomerProductInfoService mService;

    @Test
    public void add(){
        CustomerProductInfo info = new CustomerProductInfo();
        info.siteCode = "US";
        info.asin = "Asin001";
        info.rootAsin = "RootAsin001";
        info.extra = "extra001";

        mService.add(info);

    }
}
