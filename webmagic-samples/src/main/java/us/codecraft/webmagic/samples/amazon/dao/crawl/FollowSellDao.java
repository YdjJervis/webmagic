package us.codecraft.webmagic.samples.amazon.dao.crawl;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.FollowSell;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 跟卖信息 DAO
 * @date 2016/11/10 14:45
 */
@Repository
public interface FollowSellDao extends BaseDao<FollowSell> {

    List<FollowSell> findAll(FollowSell followSell);


    FollowSell find(String batchNum, String siteCode, String asin, String sellerId);
}