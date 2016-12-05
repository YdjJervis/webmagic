package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.UrlHistoryService;

import java.util.ArrayList;

public class UrlHistoryServiceTest extends SpringTestCase {

    @Autowired
    UrlHistoryService mService;

    @Test
    public void testExist() {
        System.out.println(mService.isExist("5f79bcce4b68a8b5"));
    }

    @Test
    public void testFind() {
        new ArrayList<Url>().addAll(mService.findByAsin("US", "123123"));
        System.out.println();
    }
}
