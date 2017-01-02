package com.eccang.spider.amazon.dao;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.Asin;
import com.eccang.spider.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 下架商品 Dao层
 * @date 2016/10/11 18:00
 */
@Repository
public interface NoSellDao extends BaseDao<Asin> {

    int findExist(String siteCode, String asin);

}
