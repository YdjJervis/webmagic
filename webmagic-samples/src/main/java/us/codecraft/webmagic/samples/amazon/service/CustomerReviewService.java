package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.CustomerReviewDao;
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
    CustomerReviewDao mCustomerReviewDao;

    public void add(String reviewId) {
        CustomerReview customerReview = new CustomerReview(reviewId);
        mCustomerReviewDao.add(customerReview);
    }

    public void update(CustomerReview customerReview) {
        mCustomerReviewDao.update(customerReview);
    }

    /**
     * 查询Review监听表中，状态标记为需要监听，并且还没有转换
     * 成Url的列表
     */
    public List<CustomerReview> findAll() {
        return mCustomerReviewDao.findAll();
    }

    public CustomerReview findCustomerReview(String customerCode, String reviewId) {
        return mCustomerReviewDao.findCustomerReview(customerCode, reviewId);
    }

    public boolean isExist(String customerCode, String reviewId) {
        return findCustomerReview(customerCode, reviewId) != null;
    }

    /**
     * 查询已经完成的数据
     */
    public List<CustomerReview> findNeedGenerateBatch() {
        return mCustomerReviewDao.findNeedGenerateBatch();
    }

    public void addAll(List<CustomerReview> customerReviewList) {

        List<CustomerReview> newList = new ArrayList<CustomerReview>();

        for (CustomerReview monitor : customerReviewList) {
            if (!isExist(monitor.customerCode, monitor.reviewId)) {
                newList.add(monitor);
            } else {
                update(monitor);
            }
        }

        if (CollectionUtils.isNotEmpty(newList)) {
            mCustomerReviewDao.addAll(newList);
        }
    }


}
