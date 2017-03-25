package com.eccang.spider.amazon.service.dict;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.dao.dict.IpsInfoDao;
import com.eccang.spider.amazon.dao.dict.IpsInfoManageDao;
import com.eccang.spider.amazon.pojo.dict.IpsInfo;
import com.eccang.spider.amazon.pojo.dict.IpsInfoManage;
import com.eccang.spider.amazon.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private static final Logger mLogger = LoggerFactory.getLogger(R.BusinessLog.PUBLIC);

    @Autowired
    private IpsInfoManageDao mIpsInfoManageDao;
    @Autowired
    private IpsInfoDao mIpsInfoDao;

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
    public void addIpsInfoManageAll(String host, String basCode) {
        List<IpsInfoManage> ipsInfoManageList = new ArrayList<IpsInfoManage>();
        IpsInfoManage ipsInfoManage;

        /*查询所有IP信息*/
        List<IpsInfo> ipsInfoList = (List<IpsInfo>)mIpsInfoDao.findAll();

        /*将所有IP添加到对应Host管理表中*/
        for (IpsInfo ipsInfo : ipsInfoList) {
            ipsInfoManage = new IpsInfoManage();
            ipsInfoManage.setIpInfoId(ipsInfo.getId());
            ipsInfoManage.setUrlHost(host);
            ipsInfoManage.setBasCode(basCode);
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
     * 获取正在使用的IP(当获取IP没有可使用的时)
     */
    public IpsInfoManage getUsingIp(String urlHost) {

        IpsInfoManage ipsInfoManage = null;

        /*查询数据库中固定有效代理IP信息，按时间排序取停用时间最长的IP*/
        List<IpsInfoManage> isValidIps = mIpsInfoManageDao.findIsValidIps(urlHost);

        if (isValidIps != null && isValidIps.size() > 0) {
            ipsInfoManage = isValidIps.get(0);
            updateIpToIsUsing(ipsInfoManage);
            mLogger.info("重新获取IP， date : {}",DateUtils.getNow());
        }
        return ipsInfoManage;
    }

    /**
     * 更新IP状态到正在使用中
     */
    private void updateIpToIsUsing(IpsInfoManage ipsInfoManage) {
        ipsInfoManage.setIsUsing(1);
        ipsInfoManage.setSwitchDate(new Date());
        /*设置当前使用的IP*/
        mIpsInfoManageDao.update(ipsInfoManage);

    }

    /**
     * 是否在对应域名的IP管理信息
     * @param host 域名
     */
    public boolean isExist(String host) {
        List<IpsInfoManage> ipsInfoManageList = mIpsInfoManageDao.findIpsInfoByUrlHost(host);
        return ipsInfoManageList.size() > 0;
    }


    /**
     * IP切换
     *
     * @param ipsType    代理IP类型
     * @param urlHost    需解析url的域名
     * @param statusCode 解析URL状态码
     */
    public void switchIp(String ipsType, String urlHost, int statusCode) {

        if (ipsType.equals("ipsProxy")) {
            /*获取当前正在使用的IP列表*/
            List<IpsInfoManage> ipsInfoManageList = mIpsInfoManageDao.findIpInfoIsUsing(urlHost);

            Date switchDate = null;
            if(ipsInfoManageList != null && ipsInfoManageList.size() > 0) {
                switchDate = ipsInfoManageList.get(0).getSwitchDate();
            }

            /*判断当前使用的IP是否使用时间*/
            if (ipsInfoManageList != null && ipsInfoManageList.size() > 0 && switchDate != null) {
                double useTime = (double) (System.currentTimeMillis() - switchDate.getTime()) / (double) 1000;
                if (statusCode == 407 && useTime < 20) {
                    mLogger.info("出现状态码407,IP没有使用到20s,无需切换IP.");
                    return;
                }
                if (statusCode == 0 && useTime < 30) {
                    mLogger.info("出现状态码0,IP没有使用到30s,无需切换IP.");
                    return;
                }
                if (statusCode == 402 && useTime < 10) {
                    mLogger.info("出现状态码402,IP没有使用到10s,无需切换IP.");
                    return;
                }
            }
            if(ipsInfoManageList != null && ipsInfoManageList.size() > 0) {
                /*将正在使用的IP状态更新为没使用中*/
                IpsInfoManage ipsInfoManage = new IpsInfoManage();
                ipsInfoManage.setUrlHost(urlHost);
                ipsInfoManage.setIsUsing(0);
                mIpsInfoManageDao.updateByUrlHost(ipsInfoManage);
            }
            /*固定代理IP切换IP*/
            manualSwitchIpIpsPool(urlHost);
        }
    }

    /**
     * 手动切换固定代理IP
     *
     * @param urlHost url域名
     */
    public void manualSwitchIpIpsPool(String urlHost) {

        IpsInfoManage ipsInfoManage;

        /*查询数据库中固定有效代理IP信息，按时间排序取停用时间最长的IP*/
        List<IpsInfoManage> isValidIps = mIpsInfoManageDao.findIsValidIps(urlHost);

        if (isValidIps != null && isValidIps.size() > 0) {
            ipsInfoManage = isValidIps.get(0);
            updateIpToIsUsing(ipsInfoManage);
            mLogger.info("切换IP， date : {}",DateUtils.getNow());
        } else {
            mLogger.info("固定代理IP池中，没有正在使用的IP了.");
        }
    }
}