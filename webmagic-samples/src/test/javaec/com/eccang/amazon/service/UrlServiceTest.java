package com.eccang.amazon.service;

import com.eccang.spider.base.util.UrlUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.service.UrlService;

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

        List<Url> urlList = new ArrayList<>();
        urlList.add(url);

        mUrlService.addAll(urlList);
    }

    private Url getUrl() {
        Url url = new Url();
        url.urlMD5 = UrlUtils.md5("https://www.amazon.com/bestsellers");
        url.asin = "";
        url.status = 0;
        url.priority = 0;
        url.url = "https://www.amazon.com/bestsellers";
        url.type = 6;
        url.parentUrl = "";
        url.siteCode = "US";
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
    public void testDeleteByUrlMd5(){
        mUrlService.deleteByUrlMd5("f30bd0a8a50aeb71");
    }

    @Test
    public void testFindTop100() {
        List<Url> urls = mUrlService.findTop100();
        for (Url url : urls) {
            System.out.println(url);
        }
    }
}
