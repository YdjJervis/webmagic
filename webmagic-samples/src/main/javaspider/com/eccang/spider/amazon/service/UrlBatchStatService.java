package com.eccang.spider.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.UrlBatchStatDao;
import com.eccang.spider.amazon.pojo.UrlBatchStat;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/17 9:43
 */
@Service
public class UrlBatchStatService {
    @Autowired
    private UrlBatchStatDao mUrlBatchStatDao;

    /**
     * 通过批次号查询
     */
    public List<UrlBatchStat> findByBatch(String batch) {
        return mUrlBatchStatDao.findByBatch(batch);
    }

    /**
     * 通过id查询
     */
    public UrlBatchStat findById(int id) {
        return mUrlBatchStatDao.findById(id);
    }

    /**
     * 通过批次号与url查询监测信息
     */
    public UrlBatchStat findByBatchAndUrl(String batch, String url) {
        return mUrlBatchStatDao.findByBatchAndUrl(batch, url);
    }

    /**
     * 增加一条监测信息
     */
    public void addOne(UrlBatchStat urlBatchStat) {
        mUrlBatchStatDao.addOne(urlBatchStat);
    }

    /**
     * 批量新增
     */
    public void addAll(List<UrlBatchStat> urlBatchStatList) {
        mUrlBatchStatDao.addAll(urlBatchStatList);
    }

    /**
     * 通过id更新监测信息
     */
    public void updateById(UrlBatchStat urlBatchStat) {
        mUrlBatchStatDao.updateById(urlBatchStat);
    }
}