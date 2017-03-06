package com.eccang.spider.base.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Url工具类
 * @date 2016/10/11
 */
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
     * @param url   原URL
     * @param key   get请求的参数key
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

    /**
     * url 16位MD5加密
     */
    public static String md5(String url) {
        return DigestUtils.md5Hex(url).substring(8, 24);
    }

    public static void main(String[] args) {
        System.out.println(md5("https://www.amazon.com/Best-Sellers-Software-Utilities/zgbs/software/229672/ref=zg_bs_pg_5/162-7741740-8756707?_encoding=UTF8&pg=5"));
    }
}
