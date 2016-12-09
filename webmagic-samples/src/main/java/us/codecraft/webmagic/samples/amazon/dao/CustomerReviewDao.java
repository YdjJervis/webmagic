package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.CustomerReview;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

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

    void updateByReviewIdCustomerCode(CustomerReview customerReview);

    /**
     * 查询已经完成的数据
     */
    List<CustomerReview> findNeedGenerateBatch();

}
