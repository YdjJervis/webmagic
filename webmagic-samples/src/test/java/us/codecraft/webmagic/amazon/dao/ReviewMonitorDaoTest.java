package us.codecraft.webmagic.amazon.dao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.ReviewMonitorDao;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewMonitor;

import java.util.Date;
import java.util.List;

/**
 * 功能概要：DiscussService单元测试
 *
 * @author Jervis
 */
public class ReviewMonitorDaoTest extends SpringTestCase {

    @Autowired
    private ReviewMonitorDao mDao;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void addTest() {
        ReviewMonitor monitor = new ReviewMonitor("reviewID_001");
        mDao.add(monitor);
    }

    @Test
    public void findTest() {
        mLogger.info(mDao.find("R3M72LGQOWZPQJ"));
    }

    @Test
    public void findAllTest(){
        mLogger.info(mDao.findAll());
    }

    @Test
    public void updateTest(){
        List<ReviewMonitor> list = mDao.find("R3M72LGQOWZPQJ");
        if(CollectionUtils.isNotEmpty(list)){
            for (ReviewMonitor monitor : list) {
                monitor.updatetime = new Date();
                monitor.smrMarked = 0;
                mDao.update(monitor);
            }
        }
    }

}