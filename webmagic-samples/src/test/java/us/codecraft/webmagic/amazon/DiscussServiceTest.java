package us.codecraft.webmagic.amazon;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;
import us.codecraft.webmagic.samples.amazon.service.DiscussService;
import us.codecraft.webmagic.samples.base.util.EncodeUtil;

import java.util.ArrayList;
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
        logger.info(discussList.toString());
    }

    @Test
    public void addTest() {
        Discuss discuss = new Discuss();
        discuss.setAsin("11111");
        discuss.setPerson("222223");
        discuss.setPersonID("222223");
        discuss.setTime("33333");
        discuss.setTitle(EncodeUtil.str2uni2str("不足哦排版\uD83D\uDC4E"));
        long asin = discussService.add(discuss);
        System.out.println(asin);
    }

    @Test
    public void addAll() {
        Discuss discuss = new Discuss();
        discuss.setAsin("11111");
        discuss.setPerson("222223");
        discuss.setTime("33333");
        discuss.setTitle("444445");
        discuss.setContent("6666888");

        Discuss discuss1 = new Discuss();
        discuss1.setAsin("11111");
        discuss1.setPerson("22222333");
        discuss1.setTime("33333");
        discuss1.setTitle("444445");
        discuss1.setContent("666677");

        List<Discuss> list = new ArrayList<Discuss>();
        list.add(discuss);
        list.add(discuss1);

        long asin = discussService.addAll(list);
        System.out.println(asin);
    }

    public static void main(String[] args) {
        String src = "不足哦排版\uD83D\uDC4E";
        System.out.println(EncodeUtil.str2uni2str(src));
    }
}