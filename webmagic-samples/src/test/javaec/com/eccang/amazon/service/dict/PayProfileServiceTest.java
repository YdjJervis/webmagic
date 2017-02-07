package com.eccang.amazon.service.dict;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.dict.PayProfile;
import com.eccang.spider.amazon.service.dict.PayProfileService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PayProfileServiceTest extends SpringTestCase {

    @Autowired
    private PayProfileService mService;

    @Test
    public void findByCode() {
        System.out.println(mService.findByCode("AS"));
    }

    @Test
    public void update() {
        PayProfile payProfile = mService.findByCode("AS");
        payProfile.urlPrice = 0.03f;
        mService.update(payProfile);
    }

}
