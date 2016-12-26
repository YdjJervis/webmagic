package us.codecraft.webmagic.amazon.service.dict;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.base.SpringTestCase;
import us.codecraft.webmagic.samples.amazon.pojo.dict.IpsSwitchManage;
import us.codecraft.webmagic.samples.amazon.service.dict.IpsSwitchManageService;

import java.util.Date;
import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/31 11:16
 */
public class IpsSwitchManageServiceTest  extends SpringTestCase{
    @Autowired
    IpsSwitchManageService mIpsSwitchManageService;

    @Test
    public void addTest() {
        IpsSwitchManage ipsSwitchManage = new IpsSwitchManage();
        ipsSwitchManage.setIpsType("abu");
        ipsSwitchManage.setIsComplete(0);
        ipsSwitchManage.setTotalNum(0);

        mIpsSwitchManageService.add(ipsSwitchManage);

        System.out.println(ipsSwitchManage);

    }

    @Test
    public void findByIsCompleteTest() {
        List<IpsSwitchManage> result = mIpsSwitchManageService.findByIsComplete(0);
        System.out.println(result);
    }

    @Test
    public void updateByIdTest() {
        IpsSwitchManage ipsSwitchManage = new IpsSwitchManage();
        ipsSwitchManage.setId(1);
//        ipsSwitchManage.setTotalNum(1);
//        ipsSwitchManage.setIsComplete(1);
        ipsSwitchManage.setUpdateDate(new Date());
        ipsSwitchManage.setExceptionInfo("{exceptionType:404,num:100}");
        mIpsSwitchManageService.updateById(ipsSwitchManage);
    }
}