package com.eccang.spider.amazon.dao;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.UrlBatchStat;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * 2016/11/16 16:58
 */
@Repository
public interface UrlBatchStatDao {

    /**
     * 通过批次号查询
     */
    List<UrlBatchStat> findByBatch(String batch);

    /**
     * 通过id查询
     */
    UrlBatchStat findById(int id);

    /**
     * 通过批次号与url查询监测信息
     */
    UrlBatchStat findByBatchAndUrl(String batch, String url);

    /**
     * 增加一条监测信息
     */
    void addOne(UrlBatchStat urlBatchStat);

    /**
     * 批量新增
     */
    void addAll(List<UrlBatchStat> urlBatchStatList);

    /**
     * 通过id更新监测信息
     */
    void updateById(UrlBatchStat urlBatchStat);
}
