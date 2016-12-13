package us.codecraft.webmagic.amazon.dao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.CustomerReviewDao;
import us.codecraft.webmagic.samples.amazon.pojo.CustomerReview;

import java.util.Date;
import java.util.List;

/**
 * 功能概要：DiscussService单元测试
 *
 * @author Jervis
 */
public class CustomerReviewDaoTest extends SpringTestCase {

    @Autowired
    private CustomerReviewDao mDao;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void addTest() {
        CustomerReview monitor = new CustomerReview("reviewID_001");
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
        List<CustomerReview> list = mDao.find("R3M72LGQOWZPQJ");
        if(CollectionUtils.isNotEmpty(list)){
            for (CustomerReview monitor : list) {
                monitor.updateTime = new Date();
                mDao.update(monitor);
            }
        }
    }

}