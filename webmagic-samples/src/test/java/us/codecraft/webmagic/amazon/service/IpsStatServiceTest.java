package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.downloader.AbuProxyDownloader;
import us.codecraft.webmagic.samples.amazon.pojo.IpsStat;
import us.codecraft.webmagic.samples.amazon.service.IpsStatService;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/24 14:49
 */

public class IpsStatServiceTest extends SpringTestCase{

    @Autowired
    IpsStatService mIpsStatService;

    @Autowired
    AbuProxyDownloader mDownloader;


    @Test
    public void addIpsStatTest() {
        IpsStat ipsStat = new IpsStat();
        ipsStat.setIpsStatStatus("0");
        ipsStat.setIpsChangRecord("test");
        ipsStat.setIpsStatCondition("test");
        mIpsStatService.addIpsStat(ipsStat);
        System.out.println(ipsStat.getIpsStatId());
    }

    @Test
    public void updateIpsStatByIdTest() {
        IpsStat ipsStat = new IpsStat();
        ipsStat.setIpsStatId(1);
        ipsStat.setIpsStatStatus("0");
        ipsStat.setIpsChangRecord("");
        ipsStat.setIpsStatCondition("test");
        mIpsStatService.updateIpsStatById(ipsStat);
    }

    @Test
    public void findIpsStatAllTest() {
        List<IpsStat> result = mIpsStatService.findIpsStatAll();
        System.out.println(result);
    }

    @Test
    public void findIpsStatByIdTest() {
        IpsStat ipsStat1 = mIpsStatService.findIpsStatById(1);
        System.out.println(ipsStat1);
    }

    @Test
    public void testIOC(){
        System.out.println(mIpsStatService);
        System.out.println(mDownloader);
    }
}