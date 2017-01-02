package com.eccang.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.crawl.Product;
import com.eccang.spider.amazon.service.crawl.ProductService;

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
