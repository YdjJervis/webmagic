package us.codecraft.webmagic.amazon.grammar;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.*;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/11/2 10:08
 */
public class SetTest extends TestCase {

    @Test
    public void testSetQuote() {

        Map<String, List<String>> set = new HashMap<String, List<String>>();

        List<String> list = set.get("key");
        if (list == null) {
            list = new ArrayList<String>();
            set.put("key", list);
        }

        list.add("222");

        System.out.println(set);

    }

    @Test
    public void testSet() {

        Map<String,Integer> map = new HashMap<String, Integer>();
        System.out.println(map.get("key"));
    }
}