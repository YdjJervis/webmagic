package com.eccang.amazon.service.dict;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.service.dict.ProfileService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProfileServiceTest extends SpringTestCase {

    @Autowired
    ProfileService mService;

    @Test
    public void testFindByCode() {
        System.out.println(mService.find());
    }

}
