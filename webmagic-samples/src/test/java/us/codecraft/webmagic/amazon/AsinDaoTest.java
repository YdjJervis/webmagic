package us.codecraft.webmagic.amazon;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.AsinDao;

/**
 * 功能概要：DiscussService单元测试
 *
 * @author Jervis
 */
public class AsinDaoTest extends SpringTestCase {

    @Autowired
    private AsinDao mDao;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void findTest() {
        mLogger.info(mDao.find("0-0-0-0-0"));
    }

    @Test
    public void findAllTest(){
        mLogger.info(mDao.findAll());
    }

}