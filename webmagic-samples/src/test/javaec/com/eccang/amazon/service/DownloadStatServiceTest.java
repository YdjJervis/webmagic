package com.eccang.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.DownloadStat;
import com.eccang.spider.amazon.service.DownloadStatService;

import java.util.Date;
import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/4 18:03
 */
public class DownloadStatServiceTest extends SpringTestCase {
    @Autowired
    DownloadStatService mDownloadStatService;

    @Test
    public void addTest() {
        DownloadStat downloadStat = new DownloadStat();
        downloadStat.setIpsType("ipsPool");
        downloadStat.setTotalTime(1);
        downloadStat.setValidTime(1);
        downloadStat.setInvalidTime(0);
        downloadStat.setUrlMD5("11235544");
        mDownloadStatService.add(downloadStat);
    }

    @Test
    public void findByUrlMD5Test() {
        DownloadStat downloadStat = mDownloadStatService.findByUrlMD5("11235544");
        System.out.println(downloadStat);
    }

    @Test
    public void updateByIdTest() {
        List<DownloadStat> downloadStatList = mDownloadStatService.findAll();
        if(null != downloadStatList && downloadStatList.size() > 0) {
            DownloadStat downloadStat = downloadStatList.get(0);
            downloadStat.setTotalTime(downloadStat.getTotalTime() + 1);
            downloadStat.setInvalidTime(downloadStat.getInvalidTime() + 1);
            downloadStat.setUpdateDate(new Date());
            mDownloadStatService.updateById(downloadStat);
        }
    }
}