package com.eccang.spider.amazon.service.relation;

import com.eccang.spider.amazon.dao.relation.CustomerTop100Dao;
import com.eccang.spider.amazon.pojo.relation.CustomerTop100;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * 2017/2/9 16:14
 */
@Service
public class CustomerTop100Service {

    @Autowired
    private CustomerTop100Dao mDao;

    public void add(CustomerTop100 customerTop100) {
        mDao.add(customerTop100);
    }

    public List<CustomerTop100> findNeedGenerateBatch() {
        return mDao.findNeedGenerateBatch();
    }

    public void update(CustomerTop100 customerTop100) {
        mDao.update(customerTop100);
    }

}