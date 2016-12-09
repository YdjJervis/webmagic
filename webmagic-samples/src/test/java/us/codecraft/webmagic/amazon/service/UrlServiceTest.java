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
        url.urlMD5 = "MD5001";
        mUrlService.add(url);
    }

    @Test
    public void testAddAll() {

        Url url = getUrl();

        List<Url> urlList = new ArrayList<Url>();
        urlList.add(url);

        mUrlService.addAll(urlList);
    }

    private Url getUrl() {
        Url url = new Url();
        url.urlMD5 = "Md5002";
        url.siteCode = "CN";
        url.asin = "B009P4OWJ2";
        url.status = 0;
        url.priority = 0;
        url.url = "https://www.amazon.cn/product-reviews/B00HYRXT4G";
        url.parentUrl = "test";
        url.siteCode = "CN";
        return url;
    }

    @Test
    public void testUpdate() {
        Url url = mUrlService.findByUrlMd5("8f95a3f8a7e40933");
        url.batchNum = "BatchNum001";
        mUrlService.update(url);
    }

    @Test
    public void testDeleteByAsin() {
        mUrlService.deleteByAsin("CN", "B009P4OWJ2");
    }

    @Test
    public void testFindByType() {
        System.out.println(mUrlService.find(0));
    }

    @Test
    public void testUpdateURLStatus() {
        mUrlService.resetStatus();
    }

    @Test
    public void testUpdatePriority() {
        mUrlService.updatePriority("B01LZK517R", 8);
    }
}
