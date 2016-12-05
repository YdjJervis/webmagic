package us.codecraft.webmagic.amazon.dao;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.UrlDao;
import us.codecraft.webmagic.samples.amazon.pojo.Url;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能概要：DiscussService单元测试
 *
 * @author Jervis
 */
public class UrlDaoTest extends SpringTestCase {

    @Autowired
    private UrlDao mDao;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void addTest() {
        Url url = getUrl();
        mLogger.info(mDao.add(url));
    }

    private Url getUrl() {
        Url url = new Url();
        url.url = "www.test.com";
        url.siteCode = "CN";
        url.status = 0;
        url.extra = "extra";
        url.parentUrl = "parentUrl";
        return url;
    }


    @Test
    public void addAllTest() {

        List<Url> list = new ArrayList<Url>();
        list.add(getUrl());

        mLogger.info(mDao.addAll(list));
    }

    @Test
    public void testFindByAsin() {
        mLogger.info(mDao.findByAsin("", "", "B00K0A38WM").size());
    }

}