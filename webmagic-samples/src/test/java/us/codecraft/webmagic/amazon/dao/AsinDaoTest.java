package us.codecraft.webmagic.amazon.dao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.AsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;

import java.util.Date;
import java.util.List;

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

    @Test
    public void updateTest(){
        List<Asin> asinList = mDao.find("1");
        mLogger.info(asinList);
        if(CollectionUtils.isNotEmpty(asinList)){
            for (Asin asin : asinList) {
                asin.saaStatus = "0-0-0-0-2";
                asin.updatetime = new Date();
                mDao.update(asin);
            }
        }
    }

}