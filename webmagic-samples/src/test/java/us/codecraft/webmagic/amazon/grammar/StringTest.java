package us.codecraft.webmagic.amazon.grammar;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/15 16:58
 */
public class StringTest extends TestCase {

    @Test
    public void testReplace() {
        String src = "\uD83D\uDC4D尼玛\uD83D\uDC4D";//
        System.out.println(src.replaceAll("\uD83D\uDC4D",""));
    }
}