package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.ReviewMonitorDao;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Review监听业务
 * @date 2016/10/12
 */
@Service
public class ReviewMonitorService {

    @Autowired
    ReviewMonitorDao mReviewMonitorDao;

    public void add(String reviewId) {
        ReviewMonitor reviewMonitor = new ReviewMonitor(reviewId);
        mReviewMonitorDao.add(reviewMonitor);
    }

    public void update(ReviewMonitor monitor) {
        mReviewMonitorDao.update(monitor);
    }

    /**
     * 查询Review监听表中，状态标记为需要监听，并且还没有转换
     * 成Url的列表
     */
    public List<ReviewMonitor> findAll() {
        return mReviewMonitorDao.findAll();
    }

    public ReviewMonitor findByReviewId(String reviewId) {
        return mReviewMonitorDao.findByReviewId(reviewId);
    }

    public boolean isExist(String reviewId) {
        return mReviewMonitorDao.findByReviewId(reviewId) != null;
    }


    public void addAll(List<ReviewMonitor> reviewMonitorList) {

        List<ReviewMonitor> newList = new ArrayList<ReviewMonitor>();

        for (ReviewMonitor monitor : reviewMonitorList) {
            if (!isExist(monitor.smrReviewId)) {
                newList.add(monitor);
            } else {
                update(monitor);
            }
        }

        if(CollectionUtils.isNotEmpty(newList)){
            mReviewMonitorDao.addAll(newList);
        }
    }
}
