package com.eccang.spider.amazon.dao.relation;

import com.eccang.spider.amazon.pojo.relation.CustomerPayPackage;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户和收费套餐的关系DAO
 * @date 2016/11/10 14:28
 */
@Repository
public interface CustomerPayPackageDao extends BaseDao<CustomerPayPackage> {


    List<CustomerPayPackage> findByCustomerCode(String customerCode);
}