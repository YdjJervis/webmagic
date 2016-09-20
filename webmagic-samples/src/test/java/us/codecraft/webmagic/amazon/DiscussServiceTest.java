package us.codecraft.webmagic.amazon;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.dao.DiscussDao;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;
import us.codecraft.webmagic.samples.amazon.service.DiscussService;
import us.codecraft.webmagic.samples.base.Context;

import java.util.List;

/**
 * 功能概要：DiscussService单元测试
 *
 * @author Jervis
 */
public class DiscussServiceTest extends SpringTestCase {

    @Autowired
    private DiscussService discussService;

    @Test
    public void selectUserByIdTest() {
        List<Discuss> discussList = discussService.findAll();
        logger.debug(discussList.toString());
    }

    public static void main(String[] args) {
        DiscussDao discussDao = (DiscussDao) Context.getInstance().getBean("discussDao");
        System.out.println(discussDao.findAll());
        Discuss discuss = new Discuss();
        discuss.setAsin("11111");
        discuss.setPerson("2222");
        discuss.setTime("3333");
        discuss.setTitle("4444");
        discuss.setContent("5555");

        discussDao.add(discuss);
    }

}