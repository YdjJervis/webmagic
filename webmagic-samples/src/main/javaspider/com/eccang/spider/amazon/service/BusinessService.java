package com.eccang.spider.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.BusinessDao;
import com.eccang.spider.amazon.pojo.Business;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/15 16:27
 */
@Service
public class BusinessService {

    @Autowired
    private BusinessDao mBusinessDao;

    /**
     * 新增一条数据
     */
    public void addOne(Business business) {
        mBusinessDao.addOne(business);
    }

    /**
     * 批量新增
     */
    public void addAll(List<Business> businessList) {
        mBusinessDao.addAll(businessList);
    }

    /**
     * 删除
     */
    public void delete(String businessCode) {
        mBusinessDao.delete(businessCode);
    }

    /**
     * 更新
     */
    public void update(Business business) {
        mBusinessDao.update(business);
    }

    /**
     * 批量查询
     */
    public List<Business> findAll() {
        return mBusinessDao.findAll();
    }

    /**
     * 通过业务码查询
     */
    public Business findByCode(String businessCode) {
        return mBusinessDao.findByCode(businessCode);
    }

    public boolean isExist(String businessCode) {
        return findByCode(businessCode) != null;
    }
}