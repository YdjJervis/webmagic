package com.eccang.spider.amazon.service.dict;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import com.eccang.spider.amazon.dao.dict.IpsSwitchManageDao;
import com.eccang.spider.amazon.pojo.dict.IpsSwitchManage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/31 10:56
 */
@Service
public class IpsSwitchManageService {

    @Autowired
    private IpsSwitchManageDao mIpsSwitchManageDao;

    /**
     * 新增
     * @param ipsSwitchManage IP切换管理信息对象
     */
    public void add(IpsSwitchManage ipsSwitchManage) {
        mIpsSwitchManageDao.add(ipsSwitchManage);
    }

    /**
     * 查询是否完成的IP切换管理信息（按时间分批次管理）
     *
     * @param isComplete 是否完成
     * @return IpsSwitchManage集合
     */
    public List<IpsSwitchManage> findByIsComplete(int isComplete) {
        return mIpsSwitchManageDao.findByIsComplete(isComplete);
    }

    /**
     * 通过id更新切换管理信息
     * @param ipsSwitchManage IP切换管理信息对象
     */
    public void updateById(IpsSwitchManage ipsSwitchManage) {
        mIpsSwitchManageDao.updateById(ipsSwitchManage);
    }

    /**
     * IP切换管理中的异常信息记录
     * @param page page对象
     */
    public void ipsSwitchManageExceptionRecord(Page page) {

        /*找到没有完成的切换管理(在数据库中没有完成的任务存在一个或没有)*/
        List<IpsSwitchManage> ipsSwitchManageList = mIpsSwitchManageDao.findByIsComplete(0);
        IpsSwitchManage ipsSwitchManage;

        if (ipsSwitchManageList.size() == 0) {
            /*没有，则新增*/
            ipsSwitchManage = addIpsSwitchManage((String) page.getRequest().getExtra("ipsType"));
        } else {
            ipsSwitchManage = ipsSwitchManageList.get(0);
            /*如果创建时间大于一个小时，则创建新的一个批次进行监测*/
            if((double)(System.currentTimeMillis() - ipsSwitchManage.getCreateDate().getTime())/(double) (1000*60*60) > 1) {
                /*更新旧的批次已经完成*/
                ipsSwitchManage.setIsComplete(1);
                mIpsSwitchManageDao.updateById(ipsSwitchManage);
                /*创建新的批次进行监测*/
                ipsSwitchManage = addIpsSwitchManage((String) page.getRequest().getExtra("ipsType"));
            }
        }
        int statusCode = page.getStatusCode();
        if (statusCode != 200) {
            /*获取监测记录的对应异常码出现的次数*/
            String exceptionInfo = ipsSwitchManage.getExceptionInfo();
            Map<String, Integer> map;
            String statusCodeStr = String.valueOf(statusCode);
            if (null != exceptionInfo && !"".equals(exceptionInfo)) {
                /*将字符串转化成Map对象*/
                map = new Gson().fromJson(exceptionInfo, new TypeToken<Map<String, Integer>>() {
                }.getType());
                if (map.containsKey(statusCodeStr)) {
                    /*异常状态码已经记录在监测中，则更新其出现的次数*/
                    map.put(statusCodeStr, map.get(statusCodeStr) + 1);
                } else {
                    /*异常状态码第一次出现，则将其值初始化为1*/
                    map.put(statusCodeStr, 1);
                }
            } else {
                /*监测记录的异常为空，则初始化为1*/
                map = new HashMap<>();
                map.put(statusCodeStr, 1);
            }
            /*将Map对象转化成String*/
            ipsSwitchManage.setExceptionInfo(new Gson().toJson(map));
        }
        /*更新这个批次的解析的URL总个数*/
        ipsSwitchManage.setTotalNum(ipsSwitchManage.getTotalNum() + 1);
        ipsSwitchManage.setUpdateDate(new Date());
        /*更新状态码及其对应的出现次数异常信息*/
        mIpsSwitchManageDao.updateById(ipsSwitchManage);
    }

    /**
     * 新增一条IP切换管理信息
     * @param ipsType 代理类型
     * @return IP切换管理信息对象
     */
    private IpsSwitchManage addIpsSwitchManage(String ipsType) {
        IpsSwitchManage ipsSwitchManage = new IpsSwitchManage();
        ipsSwitchManage.setIpsType(ipsType);
        ipsSwitchManage.setTotalNum(0);
        ipsSwitchManage.setIsComplete(0);
        mIpsSwitchManageDao.add(ipsSwitchManage);
        return ipsSwitchManage;
    }

    /**
     * 是否需要切换代理IP方式，是则更新切换IP管理信息状态为完成
     * @return 当前使用的代理类型
     */
    public String isSwitch() {
        String currentType = null;
        List<IpsSwitchManage> ipsSwitchManageList = mIpsSwitchManageDao.findByIsComplete(0);
        if (ipsSwitchManageList.size() == 1) {
            IpsSwitchManage ipsSwitchManage = ipsSwitchManageList.get(0);
            Long time = System.currentTimeMillis() - ipsSwitchManage.getCreateDate().getTime();
            if (time / (1000 * 60 * 60) > 1L) {
                ipsSwitchManage.setIsComplete(1);
                ipsSwitchManage.setUpdateDate(new Date());
                mIpsSwitchManageDao.updateById(ipsSwitchManage);
                Map<String, Integer> exceptionInfo = new Gson().fromJson(ipsSwitchManage.getExceptionInfo(), new TypeToken<Map<String, Integer>>() {
                }.getType());
                if (exceptionInfo.get("503") / ipsSwitchManage.getTotalNum() > 0.7) {
                    currentType = ipsSwitchManage.getIpsType();
                }
            }
        }
        return currentType;
    }
}