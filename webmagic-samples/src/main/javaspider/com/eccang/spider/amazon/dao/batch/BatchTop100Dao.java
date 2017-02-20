package com.eccang.spider.amazon.dao.batch;

import com.eccang.spider.amazon.pojo.batch.BatchTop100;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/2/9 16:28
 */
@Repository
public interface BatchTop100Dao extends BaseDao<BatchTop100> {

    List<BatchTop100> findByBatchNum(String batchNum);

    List<BatchTop100> findNotCrawl();

    BatchTop100 findByBatchNumAndSite(String batchNum, String siteCode);

    float findAverageProgress(String batchNum);
}