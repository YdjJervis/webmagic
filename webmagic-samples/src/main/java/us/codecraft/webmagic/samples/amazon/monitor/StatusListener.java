package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.samples.amazon.service.UrlService;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 状态监听器
 * @date 2016/11/1 14:11
 */
@Component
public class StatusListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UrlService mUrlService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        /* 上次强行关闭Tomcat的时候，URL状态已经是在爬取了，所以第二次启动的时候把它们重置回来 */
        mUrlService.resetStatus();
    }
}