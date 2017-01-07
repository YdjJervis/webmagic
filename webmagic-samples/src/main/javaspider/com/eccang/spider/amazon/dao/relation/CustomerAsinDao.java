package com.eccang.spider.amazon.dao.relation;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户和ASIN的关系DAO
 * @date 2016/11/10 14:28
 */
@Repository
public interface CustomerAsinDao extends BaseDao<CustomerAsin> {

    List<CustomerAsin> findByCustomerCode(String customerCode);

    CustomerAsin findByObject(CustomerAsin customerAsin);

    List<CustomerAsin> findNeedGenerateBatch();

    int findUsedCount(String customerCode);
}