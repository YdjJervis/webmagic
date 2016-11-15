package us.codecraft.webmagic.amazon.service;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfo;
import us.codecraft.webmagic.samples.amazon.service.IpsInfoService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/27 15:35
 */
public class IpsInfoServiceTest extends SpringTestCase {

    Logger sLogger = Logger.getLogger(getClass());


    @Autowired
    IpsInfoService mIpsInfoService;

    @Test
    public void findAllTest() {
        Long startTime = System.currentTimeMillis();
        List<IpsInfo> result = mIpsInfoService.findAll();
        Long endTime = System.currentTimeMillis();
        sLogger.debug("所用时间：" + (double)(endTime-startTime)/(double)1000 + "s.");
        System.out.println(result.size());
    }

    @Test
    public void findByIdTest() {
        IpsInfo ipsInfo = mIpsInfoService.findById(0);
        System.out.println(ipsInfo);
    }

    @Test
    public void findByHostTest() {
        List<IpsInfo> result = mIpsInfoService.findByHost("www.amazon.co.uk");
        System.out.println(result.size());
    }

    @Test
    public void addAllTest() {
        List<IpsInfo> addList = new ArrayList<IpsInfo>();
        IpsInfo ipsInfo = null;
        for (int i = 0; i < 4; i++) {
            ipsInfo = new IpsInfo();
            ipsInfo.setHost("www.proxy.com" + i);
            ipsInfo.setPort(88+i);
            addList.add(ipsInfo);
        }
        mIpsInfoService.addAll(addList);
    }
}
