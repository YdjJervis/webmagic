package com.eccang.spider.amazon.service.top100;

import com.eccang.spider.amazon.dao.top100.DepartmentWhitelistDao;
import com.eccang.spider.amazon.pojo.top100.DepartmentWhitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/3/15 16:14
 */
@Service
public class DepartmentWhitelistService {

    @Autowired
    DepartmentWhitelistDao mDao;

    /**
     * 添加
     */
    public void add(DepartmentWhitelist whitelist) {
        if (mDao.findByCodeCount(whitelist.depCode, whitelist.siteCode) > 0) {
            whitelist.enable = 1;
            mDao.updateByCode(whitelist);
        } else {
            mDao.add(whitelist);
        }
    }

    /**
     * 批量添加
     */
    public void addAll(List<DepartmentWhitelist> whitelists) {
        mDao.addAll(whitelists);
    }

    /**
     * 查询所有品类白名单信息
     */
    public List<DepartmentWhitelist> findAll() {
        return mDao.findAll();
    }

    /**
     * 通过品类码，站点码查询白名单信息
     */
    public DepartmentWhitelist findByDoubleCode(String depCode, String siteCode) {
        return mDao.findByDoubleCode(depCode, siteCode);
    }

    /**
     * 通过品类码统计在白名单中出现的次数
     */
    public int findByCodeCount(String depCode, String siteCode) {
        return mDao.findByCodeCount(depCode, siteCode);
    }

    /**
     * 通过id更新
     */
    public void update(DepartmentWhitelist whitelist) {
        mDao.update(whitelist);
    }

    /**
     * 通过品类码，站点码更新
     */
    public void updateByCode(DepartmentWhitelist whitelist) {
        mDao.updateByCode(whitelist);
    }

    /**
     * 通过id删除
     */
    public void delete(int id) {
        mDao.delete(id);
    }

    /**
     * 通过品类码，站点码删除
     */
    public void deleteByCode(String depCode, String siteCode) {
        mDao.deleteByCode(depCode, siteCode);
    }
}