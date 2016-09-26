package us.codecraft.webmagic.samples.base.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 编码工具
 */
public class EncodeUtil {

    /*
     * 直接\u9999表示的很少，基本都会用转义字符即\\u9999,这样一来打印出来的就不是我们需要的字符，而是Unicode码\u9999
     * 因此必须解析字符串得到想要的字符。
     */
    private static String unicode2string(String src) {
        List<String> list = new ArrayList<String>();
        String zz = "\\\\u[0-9,a-z,A-Z]{4}";

        //正则表达式用法参考API
        Pattern pattern = Pattern.compile(zz);
        Matcher m = pattern.matcher(src);
        while (m.find()) {
            list.add(m.group());
        }
        for (int i = 0, j = 2; i < list.size(); i++) {
            String st = list.get(i).substring(j, j + 4);

            //将得到的数值按照16进制解析为十进制整数，再強转为字符
            char ch = (char) Integer.parseInt(st, 16);
            //用得到的字符替换编码表达式
            src = src.replace(list.get(i), String.valueOf(ch));
        }
        return src;
    }

    /*
     * 将字符转为Unicode码表示
     */
    private static String string2unicode(String src) {
        int in;
        String st = "";
        for (int i = 0; i < src.length(); i++) {
            in = src.codePointAt(i);
            st = st + "\\u" + Integer.toHexString(in).toUpperCase();
        }
        return st;
    }

    /**
     * @param src 原字符串
     * @return 转成unicode再转到string的字符串
     */
    public static String str2uni2str(String src) {

        src = string2unicode(src);
        src = unicode2string(src);

        return src;
    }
}
