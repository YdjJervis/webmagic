package us.codecraft.webmagic.base.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.base.pojo.UserAgent;
import us.codecraft.webmagic.samples.base.service.UserAgentService;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: User-Agent业务测试
 * @date 2016/10/14 11:10
 */
public class UAServiceTest extends SpringTestCase {

    @Autowired
    private UserAgentService mService;
    @Test
    public void testFindAll(){
        System.out.println(mService.findAll());
    }

    @Test
    public void testUpdate(){
        List<UserAgent> list = mService.findAll();
        for (UserAgent userAgent : list) {
            userAgent.useCount = -1;
            mService.update(userAgent);
        }
    }

    @Test
    public void tesGetRandomUA(){
       mService.findRandomUA();
    }
}