package com.eccang.spider.amazon.dao.relation;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.relation.CustomerFollowSell;
import com.eccang.spider.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论监听Dao
 * @date 2016/10/11 18:00
 */
@Repository
public interface CustomerFollowSellDao extends BaseDao<CustomerFollowSell> {

    List<CustomerFollowSell> findNeedGenerateBatch();

    CustomerFollowSell find(String customerCode, String siteCode, String asin);

    List<CustomerFollowSell> findByCustomer(String customer);
}
