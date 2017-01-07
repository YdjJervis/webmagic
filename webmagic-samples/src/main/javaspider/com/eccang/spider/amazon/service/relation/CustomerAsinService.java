package com.eccang.spider.amazon.service.relation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.relation.CustomerAsinDao;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户-ASIN关系业务
 * @date 2016/10/11
 */
@Service
public class CustomerAsinService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private CustomerAsinDao mCustomerAsinDao;

    public List<CustomerAsin> findByCustomerCode(String customerCode) {
        return mCustomerAsinDao.findByCustomerCode(customerCode);
    }

    public void update(CustomerAsin customerAsin) {
        mCustomerAsinDao.update(customerAsin);
    }

    public void addAll(List<CustomerAsin> customerAsinList) {

        List<CustomerAsin> newList = new ArrayList<CustomerAsin>();
        for (CustomerAsin customerAsin : customerAsinList) {
            if (!isExist(customerAsin)) {
                newList.add(customerAsin);
            }
        }

        if (CollectionUtils.isNotEmpty(newList)) {
            mCustomerAsinDao.addAll(newList);
        }
    }

    public CustomerAsin find(CustomerAsin customerAsin){
        return mCustomerAsinDao.findByObject(customerAsin);
    }

    public boolean isExist(CustomerAsin customerAsin) {
        return find(customerAsin) != null;
    }


    public List<CustomerAsin> findNeedGenerateBatch() {
        return mCustomerAsinDao.findNeedGenerateBatch();
    }

    public int findUsedCount(String customerCode) {
        return mCustomerAsinDao.findUsedCount(customerCode);
    }
}
