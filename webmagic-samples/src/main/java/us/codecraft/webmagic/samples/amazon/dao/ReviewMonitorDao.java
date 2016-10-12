package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewMonitor;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论监听Dao
 * @date 2016/10/11 18:00
 */
@Repository
public interface ReviewMonitorDao extends BaseDao<ReviewMonitor> {

    ReviewMonitor findByReviewId(String reviewID);
}
