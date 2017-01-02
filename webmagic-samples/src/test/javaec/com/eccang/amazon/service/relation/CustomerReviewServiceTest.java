package com.eccang.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.relation.CustomerReview;
import com.eccang.spider.amazon.service.relation.CustomerReviewService;

public class CustomerReviewServiceTest extends SpringTestCase {

    @Autowired
    CustomerReviewService mCustomerReviewService;


    @Test
    public void addTest(){
        mCustomerReviewService.add("123456");
    }

    @Test
    public void testUpdate(){
        CustomerReview customerReview = mCustomerReviewService.findCustomerReview("","123");
        mCustomerReviewService.update(customerReview);
    }

    @Test
    public void testFind(){
        System.out.println(mCustomerReviewService.findAll());
    }
}
