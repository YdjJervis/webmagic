package us.codecraft.webmagic.amazon.service;

import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.samples.amazon.util.DateUtils;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/2 10:18
 */
public class TestMethod {

    public static synchronized void Method(String threadName) {
        System.out.println(DateUtils.getNow() + "," + threadName);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("execute finish," + DateUtils.getNow());
        }
    }

    public static void main(String[] args) {
//        ApplicationContext applicationContext =new ClassPathXmlApplicationContext("applicationContext.xml");
//        BeanDefinition bean = new GenericBeanDefinition();
//        bean.setBeanClassName("us.codecraft.webmagic.samples.amazon.dao.IpsSwitchManageDao");
//        DefaultListableBeanFactory fty = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
//        //注册Bean
//        fty.registerBeanDefinition("ipsSwitchManageDao", bean);
//
//        IpsSwitchManageDao mIpsSwitchManageDao = (IpsSwitchManageDao)applicationContext.getBean("ipsSwitchManageDao");
//        System.out.println(mIpsSwitchManageDao);
        Jedis jedis = new Jedis("127.0.0.1", 6379);
//        Jedis jedis = new Jedis("0.0.0.0", 6379);
//        Jedis jedis = new Jedis("192.168.0.121",6379);
        System.out.println(jedis.get("liugang"));
    }
}