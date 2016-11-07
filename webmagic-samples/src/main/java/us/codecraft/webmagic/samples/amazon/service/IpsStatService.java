package us.codecraft.webmagic.samples.amazon.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.IpsInfoManageDao;
import us.codecraft.webmagic.samples.amazon.dao.IpsStatDao;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfoManage;
import us.codecraft.webmagic.samples.amazon.pojo.IpsStat;
import us.codecraft.webmagic.samples.amazon.util.DateUtils;
import us.codecraft.webmagic.samples.amazon.util.ParseUtils;

import java.util.ArrayList;
import java.util.Date;
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
    private final static String SWITCH_IP_URL = "http://proxy.abuyun.com/switch-ip";
    private final static String ABU_CONDITION = "abuProxy";
    private final static String IPS_POOL_CONDITION = "ipsPool";

    @Autowired
    IpsStatDao mIpsStatDao;
    @Autowired
    IpsInfoManageDao mIpsInfoManageDao;

    /***
     * add ipsStat info
     * @param ipsStat 监测对象
     * @return int
     */
    public Integer addIpsStat(IpsStat ipsStat) {
        return this.mIpsStatDao.addIpsStat(ipsStat);
    }

    /***
     * update ipsStat by id
     * @param ipsStat 监测对象
     */
    public void updateIpsStatById(IpsStat ipsStat) {
        this.mIpsStatDao.updateIpsStatById(ipsStat);
    }

    /***
     * query all ipsStat info
     * @return IpsStat集合
     */
    public List<IpsStat> findIpsStatAll() {
        return this.mIpsStatDao.findIpsStatAll();
    }

    /***
     * query ipsStat info by id
     * @param id 数据id
     * @return 监测对象
     */
    public IpsStat findIpsStatById(Integer id) {
        return this.mIpsStatDao.findIpsStatById(id);
    }

    /**
     * manual(手动) switch ip(阿布云)
     *
     */
    public void manualSwitchIpByAbu() {

        ParseUtils parseUtils = new ParseUtils();
         /*查询监测切IP信息*/
        IpsStat ipsStat = this.findConditionAndAdd(ABU_CONDITION);

        //IpsStat ipsStat = mIpsStatDao.findIpsStatById(1);
        /*切IP服务*/
        parseUtils.parseHtmlByAbu(SWITCH_IP_URL);
        /*更新监测状态及记录IP时间*/
        this.recordSwitchIpAndUpdate(ipsStat);
        mLogger.info("切换IP， date : " + DateUtils.getNow());
    }

    /**
     * 手动切换固定代理IP
     * @param urlHost url域名
     */
    public void manualSwitchIpIpsPool(String urlHost) {

         /*查询监测切IP信息*/
        IpsStat ipsStat = this.findConditionAndAdd(IPS_POOL_CONDITION);

        IpsInfoManage ipsInfoManage;

        /*查询数据库中固定有效代理IP信息，按时间排序取停用时间最长的IP*/
        List<IpsInfoManage> isValidIps = mIpsInfoManageDao.findIsValidIps(urlHost);

        if (isValidIps != null && isValidIps.size() > 0) {
            ipsInfoManage = isValidIps.get(0);
            ipsInfoManage.setIsUsing(1);
            /*设置当前使用的IP*/
            mIpsInfoManageDao.update(ipsInfoManage);

            /*更新监测状态及记录IP切换时间*/
            this.recordSwitchIpAndUpdate(ipsStat);
            mLogger.info("切换IP， date : " + DateUtils.getNow());
        } else {
            mLogger.info("固定代理IP池中，没有正在使用的IP了.");
        }
    }

    /**
     * 记录切换IP时间，及更新监测状态
     * @param ipsStat 监测对象
     */
    private void recordSwitchIpAndUpdate(IpsStat ipsStat) {
        /*记录切换IP时的时间,并添加到数据库中*/
        String ipsChangeRecord = ipsStat.getIpsChangRecord();
        List<String> ipsChangeRecordList = new ArrayList<String>();
        if (ipsChangeRecord != null && !("").equals(ipsChangeRecord)) {
            ipsChangeRecordList = new Gson().fromJson(ipsChangeRecord, new TypeToken<List<String>>() {
            }.getType());
        }
        ipsChangeRecordList.add(DateUtils.getNow());
        ipsStat.setIpsChangRecord(new Gson().toJson(ipsChangeRecordList));
        ipsStat.setIpsStatUpdateDate(new Date());
        mIpsStatDao.updateIpsStatById(ipsStat);
    }

    /**
     * 查询代理类型是否存在，不存在则添加
     * @param condition 代理类型
     * @return IpsStat对象
     */
    private IpsStat findConditionAndAdd(String condition) {
        /*查询监测切IP信息*/
        IpsStat ipsStat = mIpsStatDao.findByCondition(condition);
        if(null == ipsStat) {
            ipsStat = new IpsStat();
            ipsStat.setIpsStatCondition(condition);
            ipsStat.setIpsStatStatus("0");
            mIpsStatDao.addIpsStat(ipsStat);
        }
        return ipsStat;
    }
}