package com.eccang.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.relation.CustomerReview;
import com.eccang.spider.amazon.service.relation.CustomerReviewService;

import java.util.ArrayList;
import java.util.List;

public class CustomerReviewServiceTest extends SpringTestCase {

    @Autowired
    CustomerReviewService mCustomerReviewService;


    @Test
    public void addTest() {
        mCustomerReviewService.add("123456");
    }

    @Test
    public void testUpdate() {
        CustomerReview customerReview = mCustomerReviewService.findCustomerReview("", "123");
        mCustomerReviewService.update(customerReview);
    }

    @Test
    public void testFind() {
        System.out.println(mCustomerReviewService.findAll());
    }

    @Test
    public void addAll() {
        List<CustomerReview> list = new ArrayList<>();

        CustomerReview customerReview = new CustomerReview();
        customerReview.asin = "B01G38UKDO";
        customerReview.reviewId = "R1XILAPZ22PKDI";
        customerReview.siteCode = "UK";
        customerReview.customerCode = "EC_001";
        list.add(customerReview);

        customerReview = new CustomerReview();
        customerReview.asin = "B01LY3AMY8";
        customerReview.reviewId = "R3N2ODIAYVFTAS";
        customerReview.siteCode = "US";
        customerReview.customerCode = "EC_001";
        list.add(customerReview);

        mCustomerReviewService.addAll(list);
    }
}
