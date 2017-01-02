package com.eccang.spider.amazon.dao.relation;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.relation.CustomerKeywordRank;
import com.eccang.spider.base.dao.BaseDao;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/26 14:15
 */
@Repository
public interface CustomerKeywordRankDao extends BaseDao<CustomerKeywordRank> {
    List<CustomerKeywordRank> findByCustomer(String customerCode);
    List<CustomerKeywordRank> findNeedGenerateBatch();
    List<CustomerKeywordRank> findCustomerCodeIsOpen(String customerCode);
    CustomerKeywordRank findByObj(CustomerKeywordRank customerKeywordRank);
}
