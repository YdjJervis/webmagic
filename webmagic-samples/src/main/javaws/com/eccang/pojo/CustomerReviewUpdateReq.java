package com.eccang.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/12 11:40
 */
public class CustomerReviewUpdateReq extends BaseReqParam {

    public List<CustomerReview> data = new ArrayList<CustomerReview>();

    public class CustomerReview{
        public String customerCode;
        public String reviewId;
        public int priority;
        public int status;
        public int frequency;
    }
}