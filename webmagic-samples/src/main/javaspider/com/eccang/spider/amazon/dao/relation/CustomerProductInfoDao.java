package com.eccang.spider.amazon.dao.relation;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.relation.CustomerProductInfo;
import com.eccang.spider.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论监听Dao
 * @date 2016/10/11 18:00
 */
@Repository
public interface CustomerProductInfoDao extends BaseDao<CustomerProductInfo> {

    CustomerProductInfo find(String customerCode, String siteCode, String asin);

}
