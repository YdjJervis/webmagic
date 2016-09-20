package us.codecraft.webmagic.amazon;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;
import us.codecraft.webmagic.samples.amazon.service.DiscussService;

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
    public void findAllTest() {
        List<Discuss> discussList = discussService.findAll();
        logger.debug(discussList.toString());
    }
}