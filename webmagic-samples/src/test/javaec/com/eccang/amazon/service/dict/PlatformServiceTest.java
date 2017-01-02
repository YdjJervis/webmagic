package com.eccang.amazon.service.dict;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.service.dict.PlatformService;

public class PlatformServiceTest extends SpringTestCase {

    @Autowired
    PlatformService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testFindByCode() {
        mLogger.info(mService.findByCode("ERP"));
    }

}
