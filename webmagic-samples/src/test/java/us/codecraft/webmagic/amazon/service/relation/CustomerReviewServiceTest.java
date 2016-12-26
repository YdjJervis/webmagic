package us.codecraft.webmagic.amazon.service.relation;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerReview;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerReviewService;

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
