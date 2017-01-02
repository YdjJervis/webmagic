package com.eccang.amazon.service.dict;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.service.dict.APIService;

public class APIServiceTest extends SpringTestCase {

    @Autowired
    APIService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testFindByCode() {
        mLogger.info(mService.findByCode("EC_001"));
    }

}
