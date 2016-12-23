package com.eccang.pojo;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 添加Review监控WS请求对象
 * @date 2016/11/17 11:47
 */
public class ReviewReq extends BaseReqParam {

    public List<Review> data = new ArrayList<>();

    public class Review {
        public String siteCode;
        public String asin;
        public String reviewId;
        public int frequency;
        public int priority;
    }

    public static void main(String[] args) {
        System.out.println(new Gson().toJson(new ReviewReq()));
    }
}