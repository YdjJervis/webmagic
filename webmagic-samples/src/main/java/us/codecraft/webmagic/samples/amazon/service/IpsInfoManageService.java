package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.IpsInfoDao;
import us.codecraft.webmagic.samples.amazon.dao.IpsInfoManageDao;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfo;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfoManage;

import java.util.ArrayList;
import java.util.Date;
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
     * @return IpsInfoManage对象
     */
    public List<IpsInfoManage> findIsValidIps(String urlHost) {
        return mIpsInfoManageDao.findIsValidIps(urlHost);
    }

    /**
     * 通过ID更新IP信息
     *
     * @param ipsInfoManage ipsInfoManage对象
     */
    public void update(IpsInfoManage ipsInfoManage) {
        mIpsInfoManageDao.update(ipsInfoManage);
    }

    /**
     * 通过url域名和正在使用的状态来更新
     */
    public void updateByUrlHost(IpsInfoManage ipsInfoManage) {
        mIpsInfoManageDao.updateByUrlHost(ipsInfoManage);
    }

    /**
     * 获取当前正在使用的ip信息
     *
     * @return IpsInfoManage
     */
    public List<IpsInfoManage> findIpInfoIsUsing(String urlHost) {
        return mIpsInfoManageDao.findIpInfoIsUsing(urlHost);
    }

    /**
     * 将正在使用的代理IP更新为准备被使用
     * @param urlHost url域名
     */
    public void updateIsUsing2PrepareUsing(String urlHost) {
        /*获取当前正在使用的IP信息；将其是否被封（isBlocked）设为true,是否正在使用（isUsing）设为false，并更新最后使用时间*/
        List<IpsInfoManage> ipsInfoManage = mIpsInfoManageDao.findIpInfoIsUsing(urlHost);
        if(ipsInfoManage == null || ipsInfoManage.size() == 0) {
            return;
        }
        IpsInfoManage infoManage = ipsInfoManage.get(0);
        infoManage.setIsBlocked(0);/*不设置被封状态*/
        infoManage.setIsUsing(0);
        mIpsInfoManageDao.updateByUrlHost(infoManage);
    }

    /**
     * 新增对应域名的所有IP管理信息
     *
     * @param host 域名
     */
    public void addIpsInfoManageAll(String host) {
        List<IpsInfoManage> ipsInfoManageList = new ArrayList<IpsInfoManage>();
        IpsInfoManage ipsInfoManage;

        /*查询所有IP信息*/
        List<IpsInfo> ipsInfoList = (List<IpsInfo>)mIpsInfoDao.findAll();

        /*将所有IP添加到对应Host管理表中*/
        for (IpsInfo ipsInfo : ipsInfoList) {
            ipsInfoManage = new IpsInfoManage();
            ipsInfoManage.setIpInfoId(ipsInfo.getId());
            ipsInfoManage.setUrlHost(host);
            ipsInfoManage.setIsBlocked(0);
            ipsInfoManage.setUsedCount(0);
            ipsInfoManageList.add(ipsInfoManage);
        }
        /*初始化一个正在使用的IP*/
        ipsInfoManageList.get(0).setIsUsing(1);
        ipsInfoManageList.get(0).setSwitchDate(new Date());
        mIpsInfoManageDao.addAll(ipsInfoManageList);
    }

    /**
     * 是否在对应域名的IP管理信息
     * @param host 域名
     */
    public boolean isExist(String host) {
        List<IpsInfoManage> ipsInfoManageList = mIpsInfoManageDao.findIpsInfoByUrlHost(host);
        return ipsInfoManageList.size() > 0;
    }
}