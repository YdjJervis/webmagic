package com.eccang.pojo.asin;

import com.eccang.pojo.BaseReqParam;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 产品爬取对象
 * @date 2016/11/17 11:47
 */
public class AsinReq extends BaseReqParam {

    public List<Asin> data = new ArrayList<>();

    public class Asin {
        public String siteCode;
        public String asin;
        public String star;
    }

    public static void main(String[] args) {
        System.out.println(new Gson().toJson(new AsinReq()));
    }
}