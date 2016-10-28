package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.IpsInfoDao;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfo;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/27 11:04
 */
@Service
public class IpsInfoService {

    @Autowired
    IpsInfoDao mIpsInfoDao;

    /**
     * 查询所有IP信息
     * @return
     */
    public List<IpsInfo> findAll() {
        return mIpsInfoDao.findAll();
    }

    /**
     * 通过ID查询IP信息
     * @param id
     * @return
     */
    public IpsInfo findById(int id) {
        return mIpsInfoDao.findById(id);
    }

    /**
     * 通过HOST查询IP信息
     * @param host
     * @return
     */
    public List<IpsInfo> findByHost(String host) {
        return mIpsInfoDao.findByHost(host);
    }

    /**
     * 批量新增IP信息
     * @param list
     * @return
     */
    public int addAll(List<IpsInfo> list) {
        return mIpsInfoDao.addAll(list);
    }
}