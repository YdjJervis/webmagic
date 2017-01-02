package com.eccang.spider.amazon.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.PushQueueDao;
import com.eccang.spider.amazon.pojo.PushQueue;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户 业务
 * @date 2016/10/11
 */
@Service
public class PushQueueService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private PushQueueDao mDao;

    public void add(PushQueue pushQueue) {
        mDao.add(pushQueue);
    }

    public void add(String batchNumber) {
        PushQueue pushQueue = new PushQueue();
        pushQueue.batchNum = batchNumber;
        add(pushQueue);
    }

    public void delete(int id) {
        mDao.delete(id);
    }

    public void update(PushQueue pushQueue) {
        mDao.update(pushQueue);
    }

    public List<PushQueue> findNeed2Push() {
        return mDao.findNeed2Push();
    }

}
