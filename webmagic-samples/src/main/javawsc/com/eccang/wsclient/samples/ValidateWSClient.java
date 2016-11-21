package com.eccang.wsclient.samples;

import com.eccang.wsclient.validate.ImageOCRService;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 验证码识别客户端调用示例
 * @date 2016/11/21 11:11
 */
public class ValidateWSClient {

    public static void main(String[] args) {
        String code = new ImageOCRService().getBasicHttpBindingIImageOCRService().getVerCodeFromUrl("https://images-na.ssl-images-amazon.com/captcha/cdkxpfei/Captcha_uwrkcvuynu.jpg", "review");
        System.out.println(code);
    }
}