package us.codecraft.webmagic.amazon;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.AsinSourceDao;

/**
 * 功能概要：DiscussService单元测试
 *
 * @author Jervis
 */
public class AsinSourceDaoTest extends SpringTestCase {

    @Autowired
    private AsinSourceDao mDao;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void findTest() {
        mLogger.info(mDao.find(1));
    }

    @Test
    public void findAllTest(){
        mLogger.info(mDao.findAll());
    }

    @Test
    public void deleteTest(){
        mLogger.info(mDao.delete(1));
    }

}