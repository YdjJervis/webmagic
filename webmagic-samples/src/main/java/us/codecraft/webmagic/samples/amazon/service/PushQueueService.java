package us.codecraft.webmagic.samples.amazon.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.PushQueueDao;
import us.codecraft.webmagic.samples.amazon.pojo.PushQueue;

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

    public void add(PushQueue pushQueue){
        mDao.add(pushQueue);
    }

}
