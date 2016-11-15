package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.ProxyAgencyBaseInfo;
import us.codecraft.webmagic.samples.amazon.service.ProxyAgencyBaseInfoService;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/15 16:58
 */
public class ProxyAgencyBaseInfoServiceTest extends SpringTestCase {

    @Autowired
    ProxyAgencyBaseInfoService mProxyAgencyBaseInfoService;

    @Test
    public void addInfoTest() {
        ProxyAgencyBaseInfo proxyAgencyBaseInfo = new ProxyAgencyBaseInfo();
        proxyAgencyBaseInfo.setProxyCode("hanyun");
        mProxyAgencyBaseInfoService.addInfo(proxyAgencyBaseInfo);
    }

    @Test
    public void deleteByIdTest() {
        mProxyAgencyBaseInfoService.deleteById(2);
    }

    @Test
    public void updateByIdTest() {
        ProxyAgencyBaseInfo proxyAgencyBaseInfo = new ProxyAgencyBaseInfo();
        proxyAgencyBaseInfo.setProxyCode("hanyuntest");
        proxyAgencyBaseInfo.setId(1);
        mProxyAgencyBaseInfoService.updateById(proxyAgencyBaseInfo);
    }

    @Test
    public void findByIdTest() {
        ProxyAgencyBaseInfo proxyAgencyBaseInfo = mProxyAgencyBaseInfoService.findById(1);
        System.out.println(proxyAgencyBaseInfo.getId() + ";" + proxyAgencyBaseInfo.getProxyCode() + ";" + proxyAgencyBaseInfo.getCreateDate() + ";" + proxyAgencyBaseInfo.getUpdateDate());
    }

    @Test
    public void findByCodeTest() {
        List<ProxyAgencyBaseInfo> proxyAgencyBaseInfoList = mProxyAgencyBaseInfoService.findByCode("hanyuntest");
        for (ProxyAgencyBaseInfo proxyAgencyBaseInfo : proxyAgencyBaseInfoList) {
            System.out.println(proxyAgencyBaseInfo.getId() + ";" + proxyAgencyBaseInfo.getProxyCode() + ";" + proxyAgencyBaseInfo.getCreateDate() + ";" + proxyAgencyBaseInfo.getUpdateDate());
        }
    }
}