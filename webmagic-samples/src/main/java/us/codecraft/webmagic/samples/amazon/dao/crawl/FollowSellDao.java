package us.codecraft.webmagic.samples.amazon.dao.crawl;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.FollowSell;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 跟卖信息 DAO
 * @date 2016/11/10 14:45
 */
@Repository
public interface FollowSellDao extends BaseDao<FollowSell>{

    FollowSell find(String siteCode, String asin, String selllerId);
}