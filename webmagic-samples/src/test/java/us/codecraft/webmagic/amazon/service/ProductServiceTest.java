package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Product;
import us.codecraft.webmagic.samples.amazon.service.crawl.ProductService;

public class ProductServiceTest extends SpringTestCase {

    @Autowired
    ProductService mService;

    @Test
    public void testAdd() {
        Product product = new Product();
        product.siteCode = "US";
        product.rootAsin = "ASIN001";
        product.modelType = 3;
        mService.add(null);
    }

}
