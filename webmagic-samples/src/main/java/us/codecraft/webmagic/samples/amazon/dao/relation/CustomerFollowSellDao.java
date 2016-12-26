package us.codecraft.webmagic.samples.amazon.dao.relation;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerFollowSell;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论监听Dao
 * @date 2016/10/11 18:00
 */
@Repository
public interface CustomerFollowSellDao extends BaseDao<CustomerFollowSell> {

    List<CustomerFollowSell> findNeedGenerateBatch();

    CustomerFollowSell find(String customerCode, String siteCode, String asin);
}
