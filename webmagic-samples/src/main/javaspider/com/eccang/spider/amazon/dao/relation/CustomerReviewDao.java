package com.eccang.spider.amazon.dao.relation;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.relation.CustomerReview;
import com.eccang.spider.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论监听Dao
 * @date 2016/10/11 18:00
 */
@Repository
public interface CustomerReviewDao extends BaseDao<CustomerReview> {

    CustomerReview findCustomerReview(String customerCode, String reviewId);

    /**
     * 查询已经完成的数据
     */
    List<CustomerReview> findNeedGenerateBatch();

    int findUsedCount(String customerCode);

    List<CustomerReview> findCustomerReviewsByCustomerCode(String customerCode);



}
