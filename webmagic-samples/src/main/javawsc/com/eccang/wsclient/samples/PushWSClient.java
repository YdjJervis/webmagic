package com.eccang.wsclient.samples;

import com.eccang.wsclient.api.Ec_Service;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 验证码识别客户端调用示例
 * @date 2016/11/21 11:11
 */
public class PushWSClient {

    public static void main(String[] args) {
        String code = new Ec_Service("http://btheb.ez-wms.com/default/svc-for-spider/wsdl").getEcSOAP().pushMessage("EC_001", "ERP", "123456789", "2017-03-11 13:00:04");
        System.out.println(code);
    }
}