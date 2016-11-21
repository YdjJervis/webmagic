package us.codecraft.webmagic.amazon.webservice;

import com.eccang.wsclient.validate.ImageOCRService;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 图片OCR识别验证
 * @date 2016/10/19 17:10
 */
public class ImageOCRTest extends TestCase{

    @Test
    public void test(){
        String code = new ImageOCRService().getBasicHttpBindingIImageOCRService().getVerCodeFromUrl("https://images-na.ssl-images-amazon.com/captcha/cdkxpfei/Captcha_uwrkcvuynu.jpg", "review");
        System.out.println(code);
    }
}