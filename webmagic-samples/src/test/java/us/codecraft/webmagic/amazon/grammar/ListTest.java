package us.codecraft.webmagic.amazon.grammar;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/11/2 11:42
 */
public class ListTest extends TestCase {

    @Test
    public void testAddAll(){
        List<String> list = new ArrayList<String>();
        list.addAll(null);

        System.out.println(list);
    }

    @Test
    public void testStringList(){
        List<String> list = new ArrayList<String>();
        list.add("  纳尼  ");

        for (String str : list) {
            str.trim();
        }

        System.out.println(list);
    }
}