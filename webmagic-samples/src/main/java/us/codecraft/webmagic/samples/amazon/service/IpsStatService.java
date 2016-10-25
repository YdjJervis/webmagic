package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.IpsStatDao;
import us.codecraft.webmagic.samples.amazon.pojo.IpsStat;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/24 13:47
 */
@Service
public class IpsStatService {

    @Autowired
    IpsStatDao mIpsStatDao;

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
     * @param type 1:IP POOL 2:阿布云接口
     * @return ipStr:ip:port
     */
    public String getCurrentIpInfo(int type) {
        String ipStr = null;

        return ipStr;
    }

    /**
     * manual(手动) switch ip
     * @param type 1:IP POOL 2:阿布云接口
     */
    public void manualSwitchIp(int type) {

    }
}