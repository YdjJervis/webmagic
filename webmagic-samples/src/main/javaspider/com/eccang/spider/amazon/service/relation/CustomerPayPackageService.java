package com.eccang.spider.amazon.service.relation;

import com.eccang.spider.amazon.dao.relation.CustomerPayPackageDao;
import com.eccang.spider.amazon.pojo.relation.CustomerPayPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户-付费套餐关系业务
 * @date 2016/10/11
 */
@Service
public class CustomerPayPackageService {

    @Autowired
    private CustomerPayPackageDao mDao;

    public void add(CustomerPayPackage customerPayPackage) {
        mDao.add(customerPayPackage);
    }

    public CustomerPayPackage findByCode(String code) {
        return mDao.findByCode(code);
    }


}
