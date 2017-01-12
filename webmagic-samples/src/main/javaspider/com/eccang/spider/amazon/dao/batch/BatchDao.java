package com.eccang.spider.amazon.dao.batch;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次DAO
 * @date 2016/11/10 14:45
 */
@Repository
public interface BatchDao extends BaseDao<Batch> {

    Batch findByBatchNumber(String batchNumber);

    List<Batch> findByStatus(int status);

    List<Batch> findByCustomer(String customerCode);
}