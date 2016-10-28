package us.codecraft.webmagic.samples.amazon.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.samples.amazon.dao.IpsInfoManageDao;
import us.codecraft.webmagic.samples.amazon.dao.IpsStatDao;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfoManage;
import us.codecraft.webmagic.samples.amazon.pojo.IpsStat;
import us.codecraft.webmagic.samples.amazon.util.DateUtils;
import us.codecraft.webmagic.samples.amazon.util.ParseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/24 13:47
 */
@Service
public class IpsStatService {

    private Logger mLogger = Logger.getLogger(getClass());
    private final static String SWITCHIPURL = "http://proxy.abuyun.com/switch-ip";

    @Autowired
    IpsStatDao mIpsStatDao;
    @Autowired
    IpsInfoManageDao mIpsInfoManageDao;

    /***
     * add ipsStat info
     * @param ipsStat
     * @return
     */
    public Integer addIpsStat(IpsStat ipsStat) {
        return this.mIpsStatDao.addIpsStat(ipsStat);
    }

    /***
     * update ipsStat by id
     * @param ipsStat
     */
    public void updateIpsStatById(IpsStat ipsStat) {
        this.mIpsStatDao.updateIpsStatById(ipsStat);
    }

    /***
     * query all ipsStat info
     * @return
     */
    public List<IpsStat> findIpsStatAll() {
        return this.mIpsStatDao.findIpsStatAll();
    }

    /***
     * query ipsStat info by id
     * @param id
     * @return
     */
    public IpsStat findIpsStatById(Integer id) {
        return this.mIpsStatDao.findIpsStatById(id);
    }

    /**
     * get current ip info
     *
     * @param type 1:IP POOL 2:阿布云接口
     * @return ipStr:ip:port
     */
    public String getCurrentIpInfo(int type) {
        String ipStr = null;

        return ipStr;
    }

    /**
     * manual(手动) switch ip
     *
     * @param type 1:阿布云接口 2:IP POOL
     */
    @Transactional
    public void manualSwitchIp(int type) {

        ParseUtils parseUtils = new ParseUtils();
         /*查询监测切IP信息*/
        IpsStat ipsStat = mIpsStatDao.findIpsStatById(type);
         /*ipsStat.getIpsStatStatus() == 1 需要切换IP*/
        if (ipsStat.getIpsStatStatus().equals("1")) {
            List<String> ipsChangeRecordList = new ArrayList<String>();
            String ipsChangeRecord = ipsStat.getIpsChangRecord();
            if (type == 2) {
                /*自己IP POOL进行切IP*/
                //1.查询数据库中固定有效代理IP信息，按时间排序取停用时间最长的IP
                List<IpsInfoManage> isValidIps = mIpsInfoManageDao.findIsValidIps();
                if(isValidIps != null && isValidIps.size() > 0) {
                    IpsInfoManage ipsInfoManage = isValidIps.get(0);
                    ipsInfoManage.setIsUsing(1);
                    mIpsInfoManageDao.update(ipsInfoManage);
                }
            } else if (type == 1) {
                /*切IP服务*/
                parseUtils.parseHtmlByAbu(SWITCHIPURL);
            }
            /*记录切换IP时的时间,并添加到数据库中*/
            if (ipsChangeRecord != null && !("").equals(ipsChangeRecord)) {
                ipsChangeRecordList = new Gson().fromJson(ipsChangeRecord, new TypeToken<List<String>>() {
                }.getType());
            }
            ipsChangeRecordList.add(DateUtils.getNow());
            ipsStat.setIpsChangRecord(new Gson().toJson(ipsChangeRecordList));
            ipsStat.setIpsStatStatus("0");
            mIpsStatDao.updateIpsStatById(ipsStat);
            mLogger.info("切换IP， date : " + DateUtils.getNow());
        } else {
            mLogger.info("IP可用无需切换， date : " + DateUtils.getNow());
        }
    }

    /***
     * 更新ip监测状态，需要切IP
     * @param type 1:阿布云接口 2:IP POOL
     */
    public void updateStatus2NeedSwitchIp(int type) {
        if(type == 2) {
            /*获取当前正在使用的IP信息；将其是否被封（isBlocked）设为ture,是否正在使用（isUsing）设为false*/
            IpsInfoManage ipsInfoManage = mIpsInfoManageDao.findIpInfoIsUsing();
            ipsInfoManage.setIsBlocked(1);
            ipsInfoManage.setIsUsing(0);
            mIpsInfoManageDao.update(ipsInfoManage);
        }
        /*更新是否需要切换IP的状态（1：需要切；0：不需要切）*/
        IpsStat ipsStat = mIpsStatDao.findIpsStatById(type);
        if (ipsStat.getIpsStatStatus().equals("0")) {
            ipsStat.setIpsStatStatus("1");
            mIpsStatDao.updateIpsStatById(ipsStat);
            mLogger.info("更新ip状态，需要切IP：true.");
        }
    }
}