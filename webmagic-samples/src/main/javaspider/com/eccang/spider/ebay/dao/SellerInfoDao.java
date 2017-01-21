package com.eccang.spider.ebay.dao;

import com.eccang.spider.ebay.pojo.SellerInfo;
import org.springframework.stereotype.Repository;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/17 10:44
 */
@Repository
public interface SellerInfoDao {
    void add(SellerInfo sellerInfo);
    int findBySellerName(String sellerName, String siteCode);
}