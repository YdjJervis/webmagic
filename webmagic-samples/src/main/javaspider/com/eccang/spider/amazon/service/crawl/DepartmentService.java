package com.eccang.spider.amazon.service.crawl;

import com.eccang.spider.amazon.dao.crawl.DepartmentDao;
import com.eccang.spider.amazon.pojo.crawl.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/16 13:37
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentDao mDao;

    /**
     * 查询所有
     */
    public List<Department> findAll(){
        return  mDao.findAll();
    }

    /**
     * 通过品类名查询
     */
    public List<Department> findByName(String depName) {
        return mDao.findByName(depName);
    }

    /**
     * 查询品类通过品类名称与父品类名称
     * @param depName 品类名
     * @param pDepName 父品类名
     */
    public Department findByNames(String depName, String pDepName) {
        return mDao.findByNames(depName, pDepName);
    }

    /**
     * 根据品类名与品类url查询品类信息
     */
    public Department findByNameAndUrl(String depName, String depUrl) {
        return mDao.findByNameAndUrl(depName, depUrl);
    }

    /**
     * 批量新增
     */
    public void addAll(List<Department> departments) {
        mDao.addAll(departments);
    }

    /**
     * 更新一条品类信息
     */
    public void update(Department department) {
        mDao.update(department);
    }
}