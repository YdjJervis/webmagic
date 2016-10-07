package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.ReviewDao;
import us.codecraft.webmagic.samples.amazon.dao.ReviewMonitorDao;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewMonitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Review业务
 */
@Service
public class ReviewService {

    @Autowired
    ReviewDao mReviewDao;
    @Autowired
    ReviewMonitorDao mReviewMonitorDao;

    public void add(Review review) {

        ReviewMonitor monitor = new ReviewMonitor(review.sarReviewId);

        mReviewDao.add(review);
        mReviewMonitorDao.add(monitor);
    }

    public void addAll(List<Review> reviewList) {

        List<ReviewMonitor> monitorList = new ArrayList<ReviewMonitor>();

        if (CollectionUtils.isNotEmpty(reviewList)) {
            for (Review review : reviewList) {
                monitorList.add(new ReviewMonitor(review.sarReviewId));
            }
        }

        mReviewDao.addAll(reviewList);
        mReviewMonitorDao.addAll(monitorList);

    }
}
