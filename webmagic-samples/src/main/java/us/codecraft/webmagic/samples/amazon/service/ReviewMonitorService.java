package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.ReviewMonitorDao;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewMonitor;

import java.util.Date;

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

    public void update(String reviewId, boolean needMonitor) {
        ReviewMonitor monitor = mReviewMonitorDao.findByReviewId(reviewId);
        monitor.smrMarked = needMonitor ? 1 : 0;
        monitor.updatetime = new Date();
        mReviewMonitorDao.update(monitor);
    }

}
