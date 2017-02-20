package com.eccang.spider.amazon.dao.relation;

import com.eccang.spider.amazon.pojo.relation.CustomerTop100;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/2/9 15:38
 */
@Repository
public interface CustomerTop100Dao extends BaseDao<CustomerTop100> {

    List<CustomerTop100> findNeedGenerateBatch();


}