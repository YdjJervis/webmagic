package com.eccang.spider.base.monitor;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 定时任务接口，此项目所有定时任务都必须实现此接口，然后在applicationContext.xml里面配置
 * @date 2016/10/11
 */
public interface ScheduledTask {

    void execute();
}
