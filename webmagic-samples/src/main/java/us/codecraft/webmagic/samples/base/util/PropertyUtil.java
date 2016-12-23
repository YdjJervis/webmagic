package us.codecraft.webmagic.samples.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 解析Properties文件工具类
 * @date 2016/12/23 13:53
 */
public class PropertyUtil {

    /**
     * @param fileName resources目录下的properties文件名称
     * @param key      文件里面的键值
     * @return 键对应Value
     */
    public static String getValue(String fileName, String key) throws Exception {

        Properties props = new Properties();
        String path = PropertyUtil.class.getResource("/" + fileName).getFile();
        path = URLDecoder.decode(path, "utf8");
        InputStream is = new FileInputStream(new File(path));
        props.load(is);
        return props.getProperty(key);
    }
}
