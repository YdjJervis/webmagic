package us.codecraft.webmagic.netsense.base.util;

public class UrlUtils {

    /**
     * @param url get请求的URL
     * @param key get请求参数的key
     * @return key的value。eg:http://data.stats.gov.cn/easyquery.htm?id=A01&dbcode=hgyd&wdcode=zb&m=getTree
     */
    public static String getValue(String url, String key) {
        String[] leftRight = url.split("\\?");
        String[] keyValues = leftRight[1].split("&");
        for (String keyValue : keyValues) {
            String[] split = keyValue.split("=");
            if (split[0].equals(key)) {
                return split[1];
            }
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(getValue("http://data.stats.gov.cn/easyquery.htm?id=A01&dbcode=hgyd&wdcode=zb&m=getTree", "id"));

    }
}
