package com.eccang.amazon.service;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.util.DateUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/2 10:18
 */
public class TestMethod extends SpringTestCase {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static synchronized void Method(String threadName) {
        System.out.println(DateUtils.getNow() + "," + threadName);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("execute finish," + DateUtils.getNow());
        }
    }

    public static void main(String[] args) {

        List<String> lists = new ArrayList<>();
        for (String str : lists) {
            System.out.println(str);
        }
    }

    @Test
    public void urlEncodeTest() {
        String urlParam = "Libros en espa&ntilde;ol";
        try {
//            urlParam = URLEncoder.encode(urlParam, "UTF-8");
            System.out.println(urlParam);
            urlParam = URLDecoder.decode(urlParam, "UTF-8");
            System.out.println(urlParam);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test_rightWayGetInfoForThrowable(){
        try {
            String s = null;
            s.toString();
        } catch (Exception e) {
            log.error("error" ,e);

        }
    }
}