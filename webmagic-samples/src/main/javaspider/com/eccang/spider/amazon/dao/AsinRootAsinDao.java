package com.eccang.spider.amazon.dao;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.relation.AsinRootAsin;
import com.eccang.spider.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.2
 * @Description: Asin和RootAsin关系 Dao层
 * @date 2016/12/6
 */
@Repository
public interface AsinRootAsinDao extends BaseDao<AsinRootAsin> {

    AsinRootAsin findByRelation(String asin,String rootAsin, String siteCode);

    AsinRootAsin findByAsin(String asin, String siteCode);
}
