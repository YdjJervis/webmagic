package us.codecraft.webmagic.netsense.stats.gov;

import junit.framework.TestCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegTest extends TestCase {

    public void testNav() {
        Pattern compile = Pattern.compile("href='(/easyquery.htm\\?cn=.*)'");
        Matcher matcher = compile.matcher("<li><a href='/easyquery.htm?cn=A01' >月度数据</a></li>");
        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }
}
