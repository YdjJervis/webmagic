package com.eccang.wsclient.samples;

import com.eccang.wsclient.api.Ec_Service;
import com.eccang.wsclient.validate.ImageOCRService;
import com.google.gson.Gson;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 验证码识别客户端调用示例
 * @date 2016/11/21 11:11
 */
public class ValidateWSClient {

    public static void main(String[] args) {
        dataPush();
    }

    public static void validateImage() {
        long startTime = System.currentTimeMillis();
        String code = new ImageOCRService().getBasicHttpBindingIImageOCRService().getVerCodeFromUrl("https://images-na.ssl-images-amazon.com/captcha/cdkxpfei/Captcha_uwrkcvuynu.jpg", "review");
        System.out.println(code);
        System.out.println(System.currentTimeMillis() - startTime);
    }

    public static void dataPush() {
        String message = new Ec_Service("http://kjtbeb.eccang.com/default/svc-for-spider/wsdl").getEcSOAP().pushMessage("", "", "", new Gson().toJson(""));
        System.out.println(message);
    }
}