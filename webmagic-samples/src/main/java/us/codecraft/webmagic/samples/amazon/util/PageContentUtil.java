package us.codecraft.webmagic.samples.amazon.util;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 处理页面内容的工具类
 * @date 2016/10/15 17:06
 */
public class PageContentUtil {

    /**
     * @param src 原字符串
     * @return 过滤特殊编码后的字符串
     */
    public static String filterBadString(String src){
        return src.replaceAll("\uD83D\uDC4D","");//过滤👍
    }
}