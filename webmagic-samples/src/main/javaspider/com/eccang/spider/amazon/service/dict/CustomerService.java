package com.eccang.spider.amazon.service.dict;

import com.eccang.spider.amazon.dao.dict.CustomerDao;
import com.eccang.spider.amazon.pojo.dict.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户 业务
 * @date 2016/10/11
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerDao mCustomerDao;

    @Autowired
    private PlatformService mPlatformService;

    public Customer findByCode(String customerCode) {
        Customer customer = mCustomerDao.findByCode(customerCode);
        if (customer != null) {
            customer.platform = mPlatformService.findByCode(customer.platformCode);
        }
        return customer;
    }


}
