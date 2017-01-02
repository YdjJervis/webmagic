package com.eccang.amazon.service.relation;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.relation.AsinRootAsin;
import com.eccang.spider.amazon.service.relation.AsinRootAsinService;

public class AsinRootAsinServiceTest extends SpringTestCase {

    @Autowired
    AsinRootAsinService mService;

    private Logger mLogger = Logger.getLogger(getClass());

    @Test
    public void testAdd() {

        AsinRootAsin asinRootAsin = new AsinRootAsin();
        asinRootAsin.asin = "Asin001";
        asinRootAsin.rootAsin = "RootAsin001";
        mService.add(asinRootAsin);
    }

}
