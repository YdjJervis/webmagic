package com.eccang.wsclient.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.pojo.PushQueue;
import com.eccang.spider.amazon.service.PushQueueService;
import com.eccang.spider.base.monitor.ScheduledTask;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/12 10:25
 */
@Service
public class PushMonitor implements ScheduledTask {

    @Autowired
    PushTask mPushTask;

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