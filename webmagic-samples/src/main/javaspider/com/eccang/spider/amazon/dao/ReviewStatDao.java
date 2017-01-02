package com.eccang.spider.amazon.dao;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.ReviewStat;
import com.eccang.spider.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Asin评论统计 Dao层
 * @date 2016/10/11 18:00
 */
@Repository
public interface ReviewStatDao extends BaseDao<ReviewStat> {

    List<ReviewStat> findByAsin(String asin);
}
