package com.eccang.pojo.review;

import com.eccang.pojo.BaseRspParam;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/23 16:55
 */
public class CusReviewRsp extends BaseRspParam {

    public List<Review> data;

    public class Review {
        public String asin;
        public String reviewId;
        public String siteCode;
        public int crawl;
        public int priority;
        public int frequency;
        public int onSell;
        public int crawlTime;
        public String finishTime;
        public String createTime;
        public String updateTime;
    }
}