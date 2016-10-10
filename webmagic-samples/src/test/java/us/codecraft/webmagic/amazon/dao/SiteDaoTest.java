package us.codecraft.webmagic.amazon.dao;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.SiteDao;

/**
 * 功能概要：DiscussService单元测试
 *
 * @author Jervis
 */
public class SiteDaoTest extends SpringTestCase {

    @Autowired
    private SiteDao mSiteDao;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void findTest() {
        mLogger.info(mSiteDao.find("JP"));
    }

    @Test
    public void findAllTest(){
        mLogger.info(mSiteDao.findAll());
    }

    @Test
    public void testFindByDomain(){
        mLogger.info(mSiteDao.findByDomain("https://www.amazon.cn"));
    }

}