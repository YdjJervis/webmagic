package com.eccang.amazon.grammar;

import com.eccang.pojo.BaseReqParam;
import com.eccang.spider.base.pojo.BasePojo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;
import com.eccang.spider.amazon.pojo.StarReviewMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Gson语法测试
 * @date 2016/10/15 15:00
 */
public class GsonTest extends TestCase {

    @Test
    public void testSetParse() {
    }

    @Test
    public void testEmpty() {
        List<StarReviewMap> list = new Gson().fromJson("", new TypeToken<List<StarReviewMap>>() {
        }.getType());
        System.out.println(list);
    }

    @Test
    public void test2Object() {
        BaseReqParam stat = new Gson().fromJson("{\"data\":[],\"cutomerCode\":\"EC_001\",\"platformCode\":\"ERP\",\"token\":\"123456789\"}", BaseReqParam.class);
        System.out.println(stat);
    }

    @Test
    public void testNull() {
        List<BasePojo> li = new ArrayList<>();

        BasePojo pojo = new BasePojo();
        pojo.extra = "extra001";

        li.add(null);
        li.add(pojo);

        System.out.println(new Gson().toJson(li));

        //result: [null,{"extra":"extra001"}]
    }

}