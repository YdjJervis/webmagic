package us.codecraft.webmagic.netsense.base.util;

import org.apache.commons.lang.StringUtils;

public class UrlUtils {

    /**
     * @param url get请求的URL
     * @param key get请求参数的key
     * @return key的value。eg:http://data.stats.gov.cn/easyquery.htm?id=A01&dbcode=hgyd&wdcode=zb&m=getTree
     */
    public static String getValue(String url, String key) {
        String[] leftRight = url.split("\\?");

        if (leftRight.length == 2) {
            String[] keyValues = leftRight[1].split("&");
            for (String keyValue : keyValues) {
                String[] split = keyValue.split("=");
                if (split[0].equals(key)) {
                    return split[1];
                }
            }
        } else {
            return "";
        }
        return "";
    }

    /**
     * @param url 原URL
     * @param key get请求的参数key
     * @param value key对应value
     * @return 添加或修改请求参数的URL
     */
    public static String setValue(String url, String key, String value) {
        if (StringUtils.isNotEmpty(getValue(url, key))) {
            String src = key + "=" + getValue(url, key);
            String des = key + "=" + value;
            return url.replace(src, des);
        } else {
            if (url.contains("=")) {
                return url + "&" + key + "=" + value;
            } else {
                return url + "?" + key + "=" + value;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(setValue("http://data.stats.gov.cn/easyquery.htm?page=3&id=2", "page", "haha"));
    }
}
