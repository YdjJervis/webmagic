package com.eccang.spider.amazon.service.batch;

import com.eccang.spider.amazon.dao.batch.BatchTop100Dao;
import com.eccang.spider.amazon.pojo.batch.BatchTop100;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * 2017/2/10 10:12
 */
@Service
public class BatchTop100Service {

    @Autowired
    private BatchTop100Dao mDao;

    public void add(BatchTop100 batchTop100) {
        mDao.add(batchTop100);
    }

    public void addAll(List<BatchTop100> batchTop100s) {
        mDao.addAll(batchTop100s);
    }

    public void update(BatchTop100 batchTop100) {
        mDao.update(batchTop100);
    }

    public List<BatchTop100> findByBatchNum(String batchNum) {
        return mDao.findByBatchNum(batchNum);
    }

    public List<BatchTop100> findNotCrawl() {
        return mDao.findNotCrawl();
    }

    public BatchTop100 findByBatchNumAndSite(String batchNum, String siteCode) {
        return mDao.findByBatchNumAndSite(batchNum, siteCode);
    }

    public float findAverageProgress(String batchNum) {
        return mDao.findAverageProgress(batchNum);
    }
}