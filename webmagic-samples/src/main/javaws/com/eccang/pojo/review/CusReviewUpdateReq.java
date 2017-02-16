package com.eccang.pojo.review;

import com.eccang.pojo.BaseReqParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/12 11:40
 */
public class CusReviewUpdateReq extends BaseReqParam {

    public List<CustomerReview> data = new ArrayList<>();

    public class CustomerReview{
        public String reviewId;
        public int crawl;
    }
}