package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.IpsInfoDao;
import us.codecraft.webmagic.samples.amazon.dao.IpsInfoManageDao;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfo;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfoManage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/27 14:53
 */
@Service
public class IpsInfoManageService {

    @Autowired
    IpsInfoManageDao mIpsInfoManageDao;
    @Autowired
    IpsInfoDao mIpsInfoDao;

    /**
     * 查询有效的IP信息
     *
     * @return
     */
    public List<IpsInfoManage> findIsValidIps() {
        return mIpsInfoManageDao.findIsValidIps();
    }

    /**
     * 通过ID更新IP信息
     *
     * @param ipsInfoManage
     */
    public void update(IpsInfoManage ipsInfoManage) {
        mIpsInfoManageDao.update(ipsInfoManage);
    }

    /**
     * 获取当前正在使用的ip信息
     *
     * @return
     */
    public IpsInfoManage findIpInfoIsUsing() {
        return mIpsInfoManageDao.findIpInfoIsUsing();
    }

    /**
     * 新增对应域名的所有IP管理信息
     *
     * @param host
     */
    public void addIpsInfoManageAll(String host) {
        List<IpsInfoManage> ipsInfoManageList = new ArrayList<IpsInfoManage>();
        IpsInfoManage ipsInfoManage = null;

        /*查询所有IP信息*/
        List<IpsInfo> ipsInfoList = mIpsInfoDao.findAll();

        /*将所有IP添加到对应Host管理表中*/
        for (IpsInfo ipsInfo : ipsInfoList) {
            ipsInfoManage = new IpsInfoManage();
            ipsInfoManage.setIpInfoId(ipsInfo.getId());
            ipsInfoManage.setUrlHost(host);
            ipsInfoManage.setIsBlocked(0);
            ipsInfoManage.setUsedCount(0);
            ipsInfoManageList.add(ipsInfoManage);
        }
        mIpsInfoManageDao.addAll(ipsInfoManageList);
    }
}