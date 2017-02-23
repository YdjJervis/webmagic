package com.eccang.spider.amazon.dao.top100;

import com.eccang.spider.amazon.pojo.top100.StockUrl;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/2/21 17:23
 */
@Repository
public interface StockUrlDao extends BaseDao<StockUrl> {

    List<StockUrl> findNeed2ParseUrl(int num);

    int findByObjCount(StockUrl stockUrl);

    StockUrl findByBatchNumAndUrlMD5(String batchNum, String urlMD5);

    void updateById(StockUrl stockUrl);
}
