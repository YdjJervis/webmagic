package com.eccang.wsclient.samples;

import com.eccang.wsclient.test.TestWSService;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 测试WebService的客户端
 * @date 2016/12/15 14:31
 */
public class TestWSClient {

    public static void main(String[] args) {

        String result = new TestWSService().getTestWSPort().doRequest("只愿得一人心，白首不分离");
        System.out.println(result);

    }
}
