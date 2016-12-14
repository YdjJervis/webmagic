package us.codecraft.webmagic.amazon.grammar;

import junit.framework.TestCase;
import org.junit.Test;
import us.codecraft.webmagic.samples.amazon.pojo.StarReviewMap;

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
        /* 测试结果：除开基础数据类型，for循环的时候每个元素都是可以改变其属性值得*/
    }

    @Test
    public void testRemoveAll(){
        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");

        List<String> list2 = new ArrayList<String>();
        list2.add("A");
        list2.add("B");
        list.removeAll(list2);

        System.out.println(list);
    }

    @Test
    public void testRemoveItem(){
        List<StarReviewMap> mapList = new ArrayList<StarReviewMap>();

        StarReviewMap map = new StarReviewMap(1,"Review001");
        StarReviewMap map2 = new StarReviewMap(2,"Review002");

        mapList.add(map);
        mapList.add(map2);

        StarReviewMap map3 = new StarReviewMap(1,"Review001");
        mapList.remove(map);

        System.out.println(mapList);

        /* 测试总结：remove是根据地址remove的，上面的String类型能Remove是因为复用了对象池 */
    }
}