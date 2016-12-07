package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.ReviewMonitorDao;
import us.codecraft.webmagic.samples.amazon.pojo.CustomerReview;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Review监听业务
 * @date 2016/10/12
 */
@Service
public class CustomerReviewService {

    @Autowired
    ReviewMonitorDao mReviewMonitorDao;

    public void add(String reviewId) {
        CustomerReview customerReview = new CustomerReview(reviewId);
        mReviewMonitorDao.add(customerReview);
    }

    public void update(CustomerReview monitor) {
        mReviewMonitorDao.update(monitor);
    }

    public void updateByReviewIdCustomerCode(CustomerReview customerReview) {
        mReviewMonitorDao.updateByReviewIdCustomerCode(customerReview);
    }

    /**
     * 查询Review监听表中，状态标记为需要监听，并且还没有转换
     * 成Url的列表
     */
    public List<CustomerReview> findAll() {
        return mReviewMonitorDao.findAll();
    }

    public CustomerReview findByReviewId(String reviewId) {
        return mReviewMonitorDao.findByReviewId(reviewId);
    }

    public boolean isExist(String reviewId) {
        return mReviewMonitorDao.findByReviewId(reviewId) != null;
    }

    public CustomerReview findByReviewIdCustomerCode(String reviewId, String customerCode) {
        return mReviewMonitorDao.findByReviewIdAndCustomerCode(reviewId, customerCode);
    }

    /**
     * 查询已经完成的数据
     */
    public List<CustomerReview> findNeedGenerateBatch() {
        return mReviewMonitorDao.findNeedGenerateBatch();
    }

    public boolean isExistInCustomerCode(String reviewId, String customerCode) {
        return mReviewMonitorDao.findByReviewIdAndCustomerCode(reviewId, customerCode) != null;
    }

    public void addAll(List<CustomerReview> customerReviewList) {

        List<CustomerReview> newList = new ArrayList<CustomerReview>();

        for (CustomerReview monitor : customerReviewList) {
            if (!isExist(monitor.reviewId)) {
                newList.add(monitor);
            } else {
                update(monitor);
            }
        }

        if (CollectionUtils.isNotEmpty(newList)) {
            mReviewMonitorDao.addAll(newList);
        }
    }
}
