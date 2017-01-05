package com.eccang.amazon.service.dict;

import com.eccang.spider.amazon.pojo.dict.Site;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.service.dict.SiteService;

public class SiteServiceTest extends SpringTestCase {

    @Autowired
    SiteService mSiteService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void findTest() {
        Site site = mSiteService.find("FR2");
        site.crawl = 1;
        mLogger.info(mSiteService.update(site));
    }


}
