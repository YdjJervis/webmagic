package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.crawl.FollowSellDao;
import us.codecraft.webmagic.samples.amazon.pojo.FollowSellMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 跟卖监控 业务
 * @date 2016/10/11
 */
@Service
public class FollowSellMonitorService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private FollowSellDao mDao;

    public void addAll(List<FollowSellMonitor> followSellMonitorList) {

        List<FollowSellMonitor> list = new ArrayList<>();
        for (FollowSellMonitor followSellMonitor : followSellMonitorList) {
            if (!isExist(followSellMonitor)) {
                list.add(followSellMonitor);
            } else {
                update(followSellMonitor);
            }
        }

        if (CollectionUtils.isNotEmpty(list)) {
            mDao.addAll(list);
        }
    }

    public void update(FollowSellMonitor followSellMonitor) {
        mDao.update(followSellMonitor);
    }

    public List<FollowSellMonitor> findNotParsed(){
        return mDao.findNotParsed();
    }

    public FollowSellMonitor findByObject(FollowSellMonitor followSellMonitor){
        return mDao.findByObject(followSellMonitor);
    }

    public boolean isExist(FollowSellMonitor followSellMonitor) {
        return findByObject(followSellMonitor) != null;
    }

}
