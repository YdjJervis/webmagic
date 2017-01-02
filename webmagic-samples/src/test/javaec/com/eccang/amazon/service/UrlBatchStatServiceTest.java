package com.eccang.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.UrlBatchStat;
import com.eccang.spider.amazon.service.UrlBatchStatService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/17 9:46
 */
public class UrlBatchStatServiceTest extends SpringTestCase {

    @Autowired
    UrlBatchStatService mUrlBatchStatService;

    @Test
    public void addOneTest() {
        UrlBatchStat urlBatchStat = new UrlBatchStat();
        urlBatchStat.setBatchNum("123456");
        urlBatchStat.setUrl("www.163.com");
        urlBatchStat.setCorrectTime(1);
        urlBatchStat.setExceptionTime(1);
        urlBatchStat.setIsProxy(1);
        mUrlBatchStatService.addOne(urlBatchStat);
    }

    @Test
    public void findByBatchTest() {
        List<UrlBatchStat> urlBatchStatList = mUrlBatchStatService.findByBatch("123456");
        for (UrlBatchStat urlBatchStat : urlBatchStatList) {
            System.out.println(urlBatchStat);
        }
    }

    @Test
    public void findByIdTest() {
        UrlBatchStat urlBatchStat = mUrlBatchStatService.findById(1);
        System.out.println(urlBatchStat);
    }

    @Test
    public void findByBatchAndUrlTest() {
        UrlBatchStat urlBatchStat = mUrlBatchStatService.findByBatchAndUrl("123456", "www.baidu.com");
        System.out.println(urlBatchStat);
    }

    @Test
    public void addAllTest() {
        List<UrlBatchStat> urlBatchStatList = new ArrayList<UrlBatchStat>();
        UrlBatchStat urlBatchStat = new UrlBatchStat();
        urlBatchStat.setBatchNum("sdac224562");
        urlBatchStat.setUrl("www.156631.com");
        urlBatchStat.setCorrectTime(1);
        urlBatchStat.setExceptionTime(1);
        urlBatchStat.setIsProxy(1);
        urlBatchStatList.add(urlBatchStat);

        urlBatchStat = new UrlBatchStat();
        urlBatchStat.setBatchNum("sdac224522");
        urlBatchStat.setUrl("www.dx422.com");
        urlBatchStat.setCorrectTime(1);
        urlBatchStat.setExceptionTime(1);
        urlBatchStat.setIsProxy(1);
        urlBatchStatList.add(urlBatchStat);

        mUrlBatchStatService.addAll(urlBatchStatList);
    }

    @Test
    public void updateByIdTest() {
        UrlBatchStat urlBatchStat = mUrlBatchStatService.findById(1);
        urlBatchStat.setCorrectTime(urlBatchStat.getCorrectTime() + 3);
        mUrlBatchStatService.updateById(urlBatchStat);
    }
}