package com.eccang.amazon.service;

import com.eccang.spider.amazon.util.DateUtils;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/2 10:18
 */
public class TestMethod {

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
        Date startTime = new Date();
        int a = 1;
        int b = 2;
        int d = a*b;
        int c;
        for (int i = 0; i < 10000; i++) {
            c = i*d;
            System.out.println(c);
        }
        Date endTime = new Date();
        System.out.println("循环内有多余操作耗时：" + (endTime.getTime() - startTime.getTime()));
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
}