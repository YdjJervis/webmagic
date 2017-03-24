package com.eccang.spider.amazon.service.top100;

import com.eccang.spider.amazon.dao.top100.DepartmentBlacklistDao;
import com.eccang.spider.amazon.pojo.top100.DepartmentBlacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/3/15 13:38
 */
@Service
public class DepartmentBlacklistService {

    @Autowired
    private DepartmentBlacklistDao mDao;

    /**
     * 添加
     */
    public void add(DepartmentBlacklist departmentBlacklist) {
        mDao.add(departmentBlacklist);
    }

    /**
     * 批量添加
     */
    public void addAll(List<DepartmentBlacklist> blacklists) {
        mDao.addAll(blacklists);
    }

    /**
     * 查询所有
     */
    public List<DepartmentBlacklist> findAll() {
        return mDao.findAll();
    }

    /**
     * 通过品类码、站点码查询品类黑名单信息
     */
    public DepartmentBlacklist findByDoubleCode(String code, String siteCode) {
        return mDao.findByDoubleCode(code, siteCode);
    }

    /**
     * 通过品类码统计在黑名单中出现的次数
     */
    public int findByDepCodeCount(String depCode, String siteCode) {
        return mDao.findByDepCodeCount(depCode, siteCode);
    }

    /**
     * 更新
     */
    public void update(DepartmentBlacklist blacklist) {
        mDao.update(blacklist);
    }

    /**
     * 通过id删除
     */
    public void delete(Integer id) {
        mDao.delete(id);
    }

    /**
     * 通过品类码，站点码删除品类黑名单
     */
    public void deleteByCode(String depCode, String siteCode) {
        mDao.deleteByCode(depCode, siteCode);
    }
}