package com.eccang.spider.amazon.service.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.batch.BatchRankDao;
import com.eccang.spider.amazon.pojo.batch.BatchRank;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/26 17:21
 */
@Service
public class BatchRankService {
    @Autowired
    BatchRankDao mBatchRankDao;

    public void addAll(List<BatchRank> batchRanks) {
        mBatchRankDao.addAll(batchRanks);
    }

    public void update(BatchRank batchRank) {
        mBatchRankDao.update(batchRank);
    }

    public List<BatchRank> findByBatch(String batchNum) {
        return mBatchRankDao.findByBatch(batchNum);
    }

    public List<BatchRank> findNotCrawled() {
        return mBatchRankDao.findNotCrawled();
    }

    public BatchRank findByObj(BatchRank batchRank) {
        return mBatchRankDao.findByObj(batchRank);
    }

    public float findAverageProgress(String batchNum) {
        return mBatchRankDao.findAverageProgress(batchNum);
    }
}