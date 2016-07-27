package us.codecraft.webmagic.netsense.tianyan.pojo;

import java.util.Random;

/**
 * 汉字
 */
public class Chinese {

    public String getChinese(long seed) throws Exception {
        String str = null;
        int highPos, lowPos;
//        seed = new Date().getTime();
        Random random = new Random(seed);
        highPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = 161 + Math.abs(random.nextInt(93));
        byte[] b = new byte[2];
        b[0] = (new Integer(highPos)).byteValue();
        b[1] = (new Integer(lowPos)).byteValue();
        str = new String(b, "GB2312");
        return str;
    }

    public static String get300Chinese() throws Exception {
        Chinese ch = new Chinese();
        String str = "";
        for (int i = 30000; i > 0; i--) {
            System.out.print(ch.getChinese(i));
//            str = str + ch.getChinese(i);
            if (i % 500 == 0) {
                System.out.println();
            }
        }
        return str;
    }

    public static void main(String[] args) throws Exception {

        get300Chinese();
    }
}
