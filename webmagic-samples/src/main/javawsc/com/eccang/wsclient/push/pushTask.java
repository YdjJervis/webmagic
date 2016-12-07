package com.eccang.wsclient.push;

import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.PushQueue;
import us.codecraft.webmagic.samples.amazon.service.BatchService;
import us.codecraft.webmagic.samples.amazon.service.PushQueueService;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/12/5 18:59
 */
public class pushTask implements Runnable {

    private PushQueue mPushQueue;

    @Autowired
    PushQueueService mPushQueueService;

    @Autowired
    BatchService mBatchService;

    public pushTask(PushQueue pushQueue) {
        this.mPushQueue = pushQueue;
    }

    @Override
    public void run() {
        /*更新此推送状态为推送中*/
        mPushQueue.status = 1;
        int pushTime = mPushQueue.times++;
        mPushQueueService.update(mPushQueue);
        /*查询当前批次下的批次状态信息*/
        Batch batch = mBatchService.findByBatchNumber(mPushQueue.batchNum);
        boolean isSuccess = push(batch);
        if(pushTime == 3) {
            mPushQueue.status = isSuccess == true ? 2 : 3;
        }
        /**/
        mPushQueueService.update(mPushQueue);
    }

    /**
     * 推送批次完成信息
     */
    private boolean push(Batch batch) {
        boolean pushResult = false;
        /*通过客户码，判断调用推送接口的方式*/
        System.out.println("推送已经完成的批量信息.");
        try {
            System.out.println(batch);
            push(batch);
        } catch (Exception e) {
            /*调用接口异常*/
        }
        return pushResult;
    }
}