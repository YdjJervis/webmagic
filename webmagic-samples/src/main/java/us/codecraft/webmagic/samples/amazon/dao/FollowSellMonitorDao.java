package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.FollowSellMonitor;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 跟卖 DAO
 * @date 2016/11/10 14:45
 */
@Repository
public interface FollowSellMonitorDao extends BaseDao<FollowSellMonitor>{

    FollowSellMonitor findByObject(FollowSellMonitor followSellMonitor);

    List<FollowSellMonitor> findNotParsed();

}