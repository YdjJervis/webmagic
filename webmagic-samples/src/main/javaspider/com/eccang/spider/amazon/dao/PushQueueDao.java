package com.eccang.spider.amazon.dao;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.PushQueue;
import com.eccang.spider.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: API DAO
 * @date 2016/11/10 14:45
 */
@Repository
public interface PushQueueDao extends BaseDao<PushQueue>{

    List<PushQueue> findNeed2Push();


}