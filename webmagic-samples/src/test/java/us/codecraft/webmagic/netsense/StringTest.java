package us.codecraft.webmagic.netsense;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 字符串测试
 */
public class StringTest extends TestCase {

    public void testList() {
        List<String> srcList = new ArrayList<String>();
        srcList.add("A");
        srcList.add("B");
        srcList.add("C");

        List<String> desList = new ArrayList<String>();
        desList.add("A");
        desList.add("B");
        desList.add("D");

        srcList.removeAll(desList);
        System.out.println(srcList);

    }

    public void testCreate() throws Exception {
        String str = null;
        int hightPos, lowPos; // 定义高低位
        Random random = new Random();
        hightPos = (176 + Math.abs(random.nextInt(39)));//获取高位值
        lowPos = (161 + Math.abs(random.nextInt(93)));//获取低位值
        byte[] b = new byte[2];
        b[0] = (new Integer(hightPos).byteValue());
        b[1] = (new Integer(lowPos).byteValue());
        str = new String(b, "GBk");//转成中文
        System.err.println(str);
    }

}
