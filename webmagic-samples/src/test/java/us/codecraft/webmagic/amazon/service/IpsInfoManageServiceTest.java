package us.codecraft.webmagic.amazon.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfoManage;
import us.codecraft.webmagic.samples.amazon.service.IpsInfoManageService;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/27 16:18
 */
public class IpsInfoManageServiceTest extends SpringTestCase {
    @Autowired
    IpsInfoManageService mIpsInfoManageService;

    @Test
    public void addIpsInfoManageAllTest() {
        mIpsInfoManageService.addIpsInfoManageAll("www.amazon.co.uk");
    }

    @Test
    public void findIsValidIpsTest() {
        List<IpsInfoManage> result = mIpsInfoManageService.findIsValidIps("www.amazon.co.uk");
        System.out.println(result.size());
    }

    @Test
    public void findIpInfoIsUsingTest() {
        IpsInfoManage ipsInfoManage = mIpsInfoManageService.findIpInfoIsUsing("www.amazon.co.uk");
        System.out.println(ipsInfoManage);
    }

    @Test
    public void updateTest() {
        IpsInfoManage ipsInfoManage = mIpsInfoManageService.findIpInfoIsUsing("www.amazon.co.uk");
        ipsInfoManage.setIsUsing(0);
        ipsInfoManage.setIsBlocked(1);
        mIpsInfoManageService.update(ipsInfoManage);
    }

}