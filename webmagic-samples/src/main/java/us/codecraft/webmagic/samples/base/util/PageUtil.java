package us.codecraft.webmagic.samples.base.util;

import org.apache.commons.io.FileUtils;
import us.codecraft.webmagic.Page;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 页面工具类
 * @date 2016/10/11
 */
public class PageUtil {

    public static void savePage(Page page, String path) {
        if (!path.endsWith("/")) {
            path += "/";
        }
        File file = new File(path + page.getUrl().get().replaceAll("/", "_").replace(":", "") + ".html");
        PrintWriter pfp = null;
        try {
            pfp = new PrintWriter(file);
            pfp.print(page.getHtml());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (pfp != null) {
                try {
                    pfp.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveImage(String url, String path) {

        if (!path.endsWith("/")) {
            path += "/";
        }

        String name = url.substring(url.lastIndexOf("/"), url.length());
        try {
            FileUtils.copyURLToFile(new URL(url), new File(path + name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        saveImage("https://images-na.ssl-images-amazon.com/captcha/druexhzz/Captcha_abyjsrvdqc.jpg","C:\\Users\\Administrator\\Desktop\\爬虫\\amazon\\验证码");
    }

}
