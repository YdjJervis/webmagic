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
    public void findTest() {
        mLogger.info(mUrlService.find(0));
    }

    @Test
    public void testAdd() {

        Url url = getUrl();
        mUrlService.add(url);
    }

    @Test
    public void testAddAll() {

        Url url = getUrl();

        List<Url> urlList = new ArrayList<Url>();
        urlList.add(url);
        urlList.add(url);

        mUrlService.addAll(urlList);
    }

    private Url getUrl() {
        Url url = new Url();
        url.siteCode = "CN";
        url.saaAsin = "B009P4OWJ2";
        url.status = 0;
        url.priority = 0;
        url.url = "https://www.amazon.cn/product-reviews/B00HYRXT4G";
        url.parentUrl = "test";
        url.siteCode = "CN";
        return url;
    }

    @Test
    public void testUpdate() {
        Url url = getUrl();
        mUrlService.update(url);
    }

    @Test
    public void testDeleteUpdateCrawl(){
        mUrlService.deleteUpdateCrawl("B0181YRLT4","one_star");
    }

    @Test
    public void testDeleteByAsin(){
        mUrlService.deleteByAsin("B00NZS00OU");
    }

    @Test
    public void testFind(){
        System.out.println(mUrlService.find(0));
    }

    @Test
    public void testUpdateURLStatus(){
        mUrlService.resetStatus();
    }
}
