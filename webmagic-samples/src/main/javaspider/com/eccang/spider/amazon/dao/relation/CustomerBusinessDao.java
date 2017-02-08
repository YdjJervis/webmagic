package com.eccang.spider.amazon.dao.relation;

import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/15 16:32
 */
@Repository
public interface CustomerBusinessDao extends BaseDao<CustomerBusiness> {

    void delete(String customerCode, String businessCode);

    List<CustomerBusiness> findAll();

    List<CustomerBusiness> findByCustomerCode(String customerCode);

    CustomerBusiness findByCode(String customerCode, String businessCode);
}
