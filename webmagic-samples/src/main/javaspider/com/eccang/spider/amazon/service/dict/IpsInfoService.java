package com.eccang.spider.amazon.service.dict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.dict.IpsInfoDao;
import com.eccang.spider.amazon.pojo.dict.IpsInfo;

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
     * @return IpsInfo对象集合
     */
    public List<IpsInfo> findAll() {
        return mIpsInfoDao.findAll();
    }

    /**
     * 通过ID查询IP信息
     * @param id id
     * @return IpsInfo对象
     */
    public IpsInfo findById(int id) {
        return mIpsInfoDao.findById(id);
    }

    /**
     * 通过HOST查询IP信息
     * @param host 域名
     * @return IpsInfo对象集合
     */
    public List<IpsInfo> findByHost(String host) {
        return mIpsInfoDao.findByHost(host);
    }

    /**
     * 批量新增IP信息
     * @param list 需要添加的IpsInfo集合
     * @return 影响行数
     */
    public int addAll(List<IpsInfo> list) {
        return mIpsInfoDao.addAll(list);
    }
}