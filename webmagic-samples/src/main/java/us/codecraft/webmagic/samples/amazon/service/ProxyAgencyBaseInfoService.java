package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.ProxyAgencyBaseInfoDao;
import us.codecraft.webmagic.samples.amazon.pojo.ProxyAgencyBaseInfo;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/15 16:46
 */
@Service
public class ProxyAgencyBaseInfoService {
    @Autowired
    ProxyAgencyBaseInfoDao mProxyAgencyBaseInfoDao;

    /**
     * 添加
     */
    public void addInfo(ProxyAgencyBaseInfo proxyAgencyBaseInfo) {
        mProxyAgencyBaseInfoDao.addInfo(proxyAgencyBaseInfo);
    }

    /**
     * 通过id删除
     */
    public void deleteById(int id) {
        mProxyAgencyBaseInfoDao.deleteById(id);
    }

    /**
     * 通过id更新
     */
    public void updateById(ProxyAgencyBaseInfo proxyAgencyBaseInfo) {
        mProxyAgencyBaseInfoDao.updateById(proxyAgencyBaseInfo);
    }

    /**
     *  通过id查询代理商信息
     */
    public ProxyAgencyBaseInfo findById(int id) {
        return mProxyAgencyBaseInfoDao.findById(id);
    }

    /**
     * 通过代理类型查询
     */
    public List<ProxyAgencyBaseInfo> findByCode(String proxyCode) {
        return mProxyAgencyBaseInfoDao.findByCode(proxyCode);
    }

    /**
     * 查询所有代理商信息
     */
    public List<ProxyAgencyBaseInfo> findAll() {
        return mProxyAgencyBaseInfoDao.findAll();
    }
}