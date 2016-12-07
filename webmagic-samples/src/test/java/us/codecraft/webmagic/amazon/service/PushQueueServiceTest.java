package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.PushQueue;
import us.codecraft.webmagic.samples.amazon.service.PushQueueService;

import java.util.List;

public class PushQueueServiceTest extends SpringTestCase {

    @Autowired
    PushQueueService mService;

    @Test
    public void testAdd() {
        PushQueue pushQueue = new PushQueue();
        pushQueue.batchNum = "Batch0001";
        pushQueue.customerCode = "Cus001";
        pushQueue.platformCode = "Plat001";
        pushQueue.status = 0;
        pushQueue.times = 3;
        pushQueue.extra = "Extra001";

        mService.add(pushQueue);
    }

    @Test
    public void delete() {
        mService.delete(4);
    }

    @Test
    public void update() {
        PushQueue pushQueue = new PushQueue();
        pushQueue.id = 1;
        pushQueue.batchNum = "Batch0001";
        pushQueue.customerCode = "Cus001";
        pushQueue.platformCode = "Plat001";
        pushQueue.status = 0;
        pushQueue.times = 2;
        pushQueue.extra = "Extra001";
        mService.update(pushQueue);
    }

    @Test
    public void findNeed2Push() {
        List<PushQueue> pushQueueList = mService.findNeed2Push();
        System.out.println(pushQueueList);
    }

}
