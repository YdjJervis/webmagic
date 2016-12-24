package com.eccang.pojo;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/23 16:55
 */
public class CustomerReviewRsp extends BaseRspParam {

    public List<Review> data;

    public class Review {
        public String asin;
        public String reviewId;
        public String siteCode;
        public int crawl;
        public int priority;
        public int frequency;
        public int noSell;
        public int crawlTime;
        public String finishTime;
        public String createTime;
        public String updateTime;
    }
}