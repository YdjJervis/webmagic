package us.codecraft.webmagic.amazon;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.UrlMonitorDao;

/**
 * 功能概要：DiscussService单元测试
 *
 * @author Jervis
 */
public class UrlMonitorDaoTest extends SpringTestCase {

    @Autowired
    private UrlMonitorDao mDao;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void findAllTest(){
        mLogger.info(mDao.findFailureAndUnCrawled());
    }

}