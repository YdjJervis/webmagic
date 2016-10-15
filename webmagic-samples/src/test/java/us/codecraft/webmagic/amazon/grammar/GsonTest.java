package us.codecraft.webmagic.amazon.grammar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;
import org.junit.Test;
import us.codecraft.webmagic.samples.amazon.pojo.StarTimeMap;

import java.util.*;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Gson语法测试
 * @date 2016/10/15 15:00
 */
public class GsonTest extends TestCase {

    @Test
    public void testSetParse() {
        StarTimeMap map = new StarTimeMap(1,new Date());
        StarTimeMap map2 = new StarTimeMap(2,new Date());

        List<StarTimeMap> list = new ArrayList<StarTimeMap>();
        list.add(map);
        list.add(map2);

        for (StarTimeMap starTime : list) {
            starTime.star = 3;
        }

        System.out.println(new Gson().toJson(list));
    }

    @Test
    public void testEmpty(){
        List<StarTimeMap> list = new Gson().fromJson("",new TypeToken<List<StarTimeMap>>(){}.getType());
        System.out.println(list);
    }


}