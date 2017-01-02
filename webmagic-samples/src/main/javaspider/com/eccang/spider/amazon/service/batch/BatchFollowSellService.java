package com.eccang.spider.amazon.service.batch;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.batch.BatchFollowSellDao;
import com.eccang.spider.amazon.pojo.batch.BatchFollowSell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 跟卖批次详单 业务
 * @date 2016/10/11
 */
@Service
public class BatchFollowSellService {

    @Autowired
    private BatchFollowSellDao mDao;

    public void addAll(List<BatchFollowSell> batchFollowSellList) {

        List<BatchFollowSell> newList = new ArrayList<>();

        for (BatchFollowSell batchFollowSell : batchFollowSellList) {
            if (!isExist(batchFollowSell.batchNumber, batchFollowSell.siteCode, batchFollowSell.asin)) {
                newList.add(batchFollowSell);
            } else {
                update(batchFollowSell);
            }
        }

        if (CollectionUtils.isNotEmpty(newList)) {
            mDao.addAll(newList);
        }
    }

    public void update(BatchFollowSell batchFollowSell) {
        mDao.update(batchFollowSell);
    }

    public boolean isExist(String batchNum, String siteCode, String asin) {
        return find(batchNum, siteCode, asin) != null;
    }

    public BatchFollowSell find(String batchNum, String siteCode, String asin) {
        return mDao.find(batchNum, siteCode, asin);
    }

    public void updateAll(List<BatchFollowSell> batchFollowSellList) {
        for (BatchFollowSell batchFollowSell : batchFollowSellList) {
            update(batchFollowSell);
        }
    }

    public List<BatchFollowSell> findAllByBatchNum(String number) {
        return mDao.findAllByBatchNum(number);
    }

    public List<BatchFollowSell> findNotCrawled() {
        return mDao.findNotCrawled();
    }

    public float findAverageProgress(String batchNum) {
        return mDao.findAverageProgress(batchNum);
    }

    /**
     * 把详单记录设置成爬取完毕状态
     */
    public void updateCrawlFinish(String batchNum, String siteCode, String asin){
        mDao.updateCrawlFinish(batchNum, siteCode, asin);
    }
}
