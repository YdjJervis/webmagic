package com.eccang.spider.amazon.dao;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.Asin;
import com.eccang.spider.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Asin Dao层
 * @date 2016/10/11 18:00
 */
@Repository
public interface AsinDao extends BaseDao<Asin> {

    Asin findByAsin(String siteCode, String asin);

}
