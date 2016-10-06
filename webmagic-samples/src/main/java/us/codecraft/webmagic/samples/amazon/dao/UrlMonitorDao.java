package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.UrlMonitor;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * 网站字典表Dao
 */
@Repository
public interface UrlMonitorDao extends BaseDao<UrlMonitor> {

    List<UrlMonitor> findFailureAndUnCrawled();
}
