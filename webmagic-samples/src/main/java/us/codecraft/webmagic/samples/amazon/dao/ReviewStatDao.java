package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewStat;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Asin评论统计 Dao层
 * @date 2016/10/11 18:00
 */
@Repository
public interface ReviewStatDao extends BaseDao<ReviewStat> {

    List<ReviewStat> findByAsin(String asin);
}
