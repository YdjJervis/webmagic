package us.codecraft.webmagic.samples.amazon.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论的时间处理类，把所有时间都标准化成统一的格式
 * @date 2016/10/12 18:05
 */
public class ReviewTimeUtil {

    public static Date parmat(String time, String siteCode) {

        Date date = null;
        //于 2015年11月19日 || 投稿日 2016/9/29
        if ("CN".equals(siteCode) || "JP".equals(siteCode)) {
            Matcher matcher = Pattern.compile(".*([0-9]{4}).{1}([0-9]+).{1}([0-9]+)").matcher(time);
            if (matcher.find()) {
                date = new Date();
                date.setYear(get(matcher.group(1), "y"));
                date.setMonth(get(matcher.group(2), "m"));
                date.setDate(get(matcher.group(3), "d"));
                System.out.println(date);
            }
        } else if ("US".equals(siteCode) || "CA".equals(siteCode)) { // on August 12, 2016
            time = replaceUSTime(time);// on 12 8 2016
            Matcher matcher = Pattern.compile(".*([0-9]+).{1}([0-9]+).{1}([0-9]{4})").matcher(time);
            if (matcher.find()) {
                date = new Date();
                date.setYear(get(matcher.group(3), "y"));
                date.setMonth(get(matcher.group(1), "m"));
                date.setDate(get(matcher.group(2), "d"));
                System.out.println(date);
            }
        } else if ("DE".equals(siteCode)) { // am 4. Mai 2016
            time = replaceDETime(time);// am 4 5 2016
            Matcher matcher = Pattern.compile(".*([0-9]+).{1}([0-9]+).{1}([0-9]{4})").matcher(time);
            if (matcher.find()) {
                date = new Date();
                date.setYear(get(matcher.group(3), "y"));
                date.setMonth(get(matcher.group(2), "m"));
                date.setDate(get(matcher.group(1), "d"));
                System.out.println(date);
            }
        } else if ("MX".equals(siteCode)) { // el 1 de junio de 2016 墨西哥
            time = replaceMXTime(time);// el 1 6 2016 墨西哥
            Matcher matcher = Pattern.compile(".*\\s([0-9]+).{1}([0-9]+).{1}([0-9]{4})").matcher(time);
            if (matcher.find()) {
                date = new Date();
                date.setYear(get(matcher.group(3), "y"));
                date.setMonth(get(matcher.group(2), "m"));
                date.setDate(get(matcher.group(1), "d"));
                System.out.println(date);
            }
        }

        return new Date();
    }

    private static String replaceUSTime(String time) {
        time = time.replace(",", "");
        time = time.replace("January", "1");
        time = time.replace("February", "2");
        time = time.replace("March", "3");
        time = time.replace("April", "4");
        time = time.replace("May", "5");
        time = time.replace("June", "6");
        time = time.replace("July", "7");
        time = time.replace("August", "8");
        time = time.replace("September", "9");
        time = time.replace("October", "10");
        time = time.replace("November", "11");
        time = time.replace("December", "12");
        return time;
    }

    private static String replaceDETime(String time) {
        time = time.replace(".", "");
        time = time.replace("Januar", "1");
        time = time.replace("Februar", "2");
        time = time.replace("März", "3");
        time = time.replace("April", "4");
        time = time.replace("Mai", "5");
        time = time.replace("Juni", "6");
        time = time.replace("Juli", "7");
        time = time.replace("August", "8");
        time = time.replace("September", "9");
        time = time.replace("Oktober", "10");
        time = time.replace("November", "11");
        time = time.replace("Dezember", "12");
        return time;
    }

    private static String replaceMXTime(String time) {
        time = time.replace("de ", "");
        time = time.replace("enero", "1");
        time = time.replace("febrero", "2");
        time = time.replace("marzo", "3");
        time = time.replace("abril", "4");
        time = time.replace("mayo", "5");
        time = time.replace("junio", "6");
        time = time.replace("Juli", "7");
        time = time.replace("agosto", "8");
        time = time.replace("septiembre", "9");
        time = time.replace("octubre", "10");
        time = time.replace("noviembre", "11");
        time = time.replace("diciembre", "12");
        return time;
    }

    private static int get(String number, String flag) {
        if ("y".equals(flag)) {
            return Integer.valueOf(number) - 1900;
        } else if ("m".equals(flag)) {
            return Integer.valueOf(number) - 1;
        } else {
            return Integer.valueOf(number);
        }
    }

    public static void main(String[] args) {
        parmat("于 2015年11月19日", "CN");
        parmat("投稿日 2016/9/29", "JP");
        parmat("on August 12, 2016", "US");
        parmat("am 4. Mai 2016", "DE");
        parmat("el 28 de mayo de 2016","MX");
    }

}