package com.eccang.amazon.service;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.service.BusinessService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/3/9 15:56
 */
public class BusinessServiceTest extends SpringTestCase {

    @Autowired
    private BusinessService mService;

    @Test
    public void findByCode(){
        System.out.println(mService.findByCode("AS"));
    }
}
