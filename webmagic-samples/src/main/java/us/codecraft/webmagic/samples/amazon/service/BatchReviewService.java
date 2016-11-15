package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.BatchReviewDao;
import us.codecraft.webmagic.samples.amazon.pojo.BatchReview;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 批次Review监控 业务
 * @date 2016/10/11
 */
@Service
public class BatchReviewService {

    @Autowired
    private BatchReviewDao mBatchReviewDao;

    public void addAll(List<BatchReview> batchReviewList) {

        List<BatchReview> newList = new ArrayList<BatchReview>();

        for (BatchReview batchReview : batchReviewList) {
            if (!isExist(batchReview)) {
                newList.add(batchReview);
            } else {
                update(batchReview);
            }
        }

        if (CollectionUtils.isNotEmpty(newList)) {
            mBatchReviewDao.addAll(newList);
        }
    }

    public void update(BatchReview batchReview) {
        mBatchReviewDao.update(batchReview);
    }

    public boolean isExist(BatchReview batchAsin) {
        return mBatchReviewDao.find(batchAsin) != null;
    }

    public List<BatchReview> findByReviewID(String reviewID) {
        return mBatchReviewDao.findByReviewID(reviewID);
    }

    public void updateAll(List<BatchReview> batchReviewList) {
        for (BatchReview batchReview : batchReviewList) {
            update(batchReview);
        }
    }

}
