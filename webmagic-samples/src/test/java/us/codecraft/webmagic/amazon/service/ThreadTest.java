package us.codecraft.webmagic.amazon.service;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/2 10:00
 */
public class ThreadTest implements Runnable {

    private TestMethod mTestMethod = new TestMethod();

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        mTestMethod.Method(threadName);
    }


    public static void main(String[] args) {
        ThreadTest threadTest1 = new ThreadTest();
        ThreadTest threadTest2 = new ThreadTest();
        Thread thread1 = new Thread(threadTest1, "test1");
        Thread thread2 = new Thread(threadTest2, "test2");
        thread1.start();
        thread2.start();
    }
}