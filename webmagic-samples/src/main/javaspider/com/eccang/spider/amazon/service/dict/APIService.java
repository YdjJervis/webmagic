package com.eccang.spider.amazon.service.dict;

import com.eccang.spider.amazon.dao.dict.APIDao;
import com.eccang.spider.amazon.pojo.dict.API;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户 业务
 * @date 2016/10/11
 */
@Service
public class APIService {

    @Autowired
    private APIDao mAPIDao;

    @Autowired
    private CustomerService mCustomerService;

    public API findByCode(String customerCode) {
        API api = mAPIDao.findByCode(customerCode);
        if (api != null) {
            api.customer = mCustomerService.findByCode(customerCode);
        }
        return api;
    }


}
