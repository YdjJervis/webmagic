package com.eccang.wsclient.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.PushQueue;
import us.codecraft.webmagic.samples.amazon.service.PushQueueService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/12 10:25
 */
@Service
public class PushMonitor implements ScheduledTask{

    @Autowired
    pushTask mPushTask;

    @Autowired
    PushQueueService mPushQueueService;

    @Override
    public void execute() {
        List<PushQueue> pushQueueList = mPushQueueService.findNeed2Push();
        for (PushQueue pushQueue : pushQueueList) {
            mPushTask.startTask(pushQueue);
        }
    }
}