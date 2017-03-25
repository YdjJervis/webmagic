package com.eccang.spider.amazon.service.relation;

import com.eccang.spider.amazon.dao.relation.CustomerProductInfoDao;
import com.eccang.spider.amazon.pojo.relation.CustomerProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户-产品基本信息关系业务
 * @date 2016/10/11
 */
@Service
public class CustomerProductInfoService {

    @Autowired
    private CustomerProductInfoDao mDao;

    public void add(CustomerProductInfo customerProductInfo) {
        if(isExist(customerProductInfo.customerCode,customerProductInfo.siteCode,customerProductInfo.asin)){
            mDao.update(customerProductInfo);
        }else{
            mDao.add(customerProductInfo);
        }
    }

    public boolean isExist(String customerCode, String siteCode, String asin) {
        return mDao.find(customerCode, siteCode, asin) != null;
    }
}
