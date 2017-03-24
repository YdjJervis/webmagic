package com.eccang.spider.amazon.service.crawl;

import com.eccang.spider.amazon.dao.crawl.DepartmentDao;
import com.eccang.spider.amazon.pojo.crawl.Department;
import org.apache.commons.collections.CollectionUtils;
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

    public List<Department> findByPId(int pid) {
        return mDao.findByPId(pid);
    }

    /**
     * 批量新增
     */
    public void addAll(List<Department> departments) {

        if(CollectionUtils.isEmpty(departments) || departments.size() == 0) {
            return;
        }

        for (Department department : departments) {
            mDao.add(department);
        }
    }

    /**
     * 更新一条品类信息
     */
    public void update(Department department) {
        mDao.update(department);
    }

    /**
     * 通过批次号与品类url查询品类信息
     */
    public Department findByBatchNumAndDepUrl(String batchNum, String depUrl) {
        return mDao.findByBatchNumAndDepUrl(batchNum, depUrl);
    }

    /**
     * 通过批次号与品类码查询品类信息
     */
    public Department findByBatchNumAndDepCode(String batchNum, String depCode) {
        return mDao.findByBatchNumAndDepCode(batchNum, depCode);
    }
}