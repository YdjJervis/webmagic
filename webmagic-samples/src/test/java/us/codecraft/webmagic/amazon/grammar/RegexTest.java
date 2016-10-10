package us.codecraft.webmagic.amazon.grammar;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 亚马逊精抽取正则相关测试
 */
public class RegexTest extends TestCase{

    /**
     * 测试class属性的value中提取星级
     */
    @Test
    public void testStart(){
        Matcher matcher = Pattern.compile(".*-([0-5]).*").matcher("a-icon a-icon-star a-star-3 review-rating");

        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}
