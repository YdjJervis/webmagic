package com.eccang.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: Review查询返回
 * @String 2016/11/21 16:52
 */
public class ReviewQueryRsp extends BaseRspParam {

    public int pageTotal;
    public int pageNum;
    public int pageSize;
    public List<Review> data = new ArrayList<Review>();

    public class Review {
        public String siteCode;
        public String asin;
        public String time;
        public String personID;
        public String reviewID;
        public String buyStatus;
        public int star;
        public String title;
        public String content;
    }
}