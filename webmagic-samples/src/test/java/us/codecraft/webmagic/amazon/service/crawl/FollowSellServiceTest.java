package us.codecraft.webmagic.amazon.service.crawl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.FollowSell;
import us.codecraft.webmagic.samples.amazon.service.crawl.FollowSellService;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/1/2 14:19
 */
public class FollowSellServiceTest extends SpringTestCase {

    @Autowired
    private FollowSellService mService;

    @Test
    public void findAll(){
        FollowSell followSell = new FollowSell();
        followSell.batchNum = "Batch001";
        System.out.println(mService.findAll(followSell));
    }
}
