package us.codecraft.webmagic.netsense;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 单例模式，获取Spring的ApplicationContext
 */
public class Context {

    private static ApplicationContext mContext;

    private Context(){}

    public static synchronized ApplicationContext getInstance(){
        if(mContext==null){
            mContext = new ClassPathXmlApplicationContext("netsence/applicationContext.xml");
        }
        return mContext;
    }
}
