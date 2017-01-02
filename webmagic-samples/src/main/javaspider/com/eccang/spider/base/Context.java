package com.eccang.spider.base;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring容器加载
 */
public class Context {

    private static ApplicationContext mContext;

    private Context() {
    }

    public static synchronized ApplicationContext getInstance() {
        if (mContext == null) {
            mContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        }
        return mContext;
    }
}
