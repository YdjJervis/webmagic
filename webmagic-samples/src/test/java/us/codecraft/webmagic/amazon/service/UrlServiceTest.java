package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.UrlService;

import java.util.ArrayList;
import java.util.List;

public class UrlServiceTest extends SpringTestCase {

    @Autowired
    UrlService mUrlService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void findTest(){
        mLogger.info(mUrlService.findFailures());
    }

    @Test
    public void testAdd(){

        Url url = getUrl();
        mUrlService.add(url);
    }

    @Test
    public void testAddAll(){

        Url url = getUrl();

        List<Url> urlList = new ArrayList<Url>();
        urlList.add(url);

        mUrlService.addAll(urlList);
    }

    private Url getUrl() {
        Url url = new Url();
        url.url = "https://www.amazon.cn/product-reviews/B00HYRXT4G";
        url.parentUrl = "test";
        url.siteCode = "CN";
        return url;
    }

    @Test
    public void testUpdate(){
        Url url = getUrl();
        mUrlService.update(url);
    }

}
