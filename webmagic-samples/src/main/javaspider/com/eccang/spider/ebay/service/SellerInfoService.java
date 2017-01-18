package com.eccang.spider.ebay.service;

import com.eccang.spider.ebay.dao.SellerInfoDao;
import com.eccang.spider.ebay.pojo.SellerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/17 10:51
 */
@Service
public class SellerInfoService {
    @Autowired
    SellerInfoDao mDao;

    public void add(SellerInfo sellerInfo){
        mDao.add(sellerInfo);
    }

    public boolean isExistSeller(String sellerName) {
        return mDao.findBySellerName(sellerName) != null;
    }
}