package us.codecraft.webmagic.netsense.tianyan.util;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class ThreadUtil {
    /**
     * @param timeMin 分钟数
     */
    public static void sleep(long timeMin) {
        try {
            Thread.sleep(timeMin * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
