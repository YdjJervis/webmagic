package com.eccang.spider.amazon.service.relation;

import com.eccang.spider.amazon.dao.relation.CustomerPayPackageDao;
import com.eccang.spider.amazon.pojo.relation.CustomerPayPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CustomerPayPackage> findByCustomerCode(String customerCode) {
        return mDao.findByCustomerCode(customerCode);
    }

    public CustomerPayPackage findActived(String customerCode) {
        return mDao.findActived(customerCode);
    }

    public CustomerPayPackage find(String customerCode, String payPackageCode) {
        return mDao.find(customerCode, payPackageCode);
    }

    public boolean isExist(String customerCode, String payPackageCode) {
        return find(customerCode, payPackageCode) != null;
    }

    public void cancelAll(String customerCode) {
        mDao.cancelAll(customerCode);
    }


}
