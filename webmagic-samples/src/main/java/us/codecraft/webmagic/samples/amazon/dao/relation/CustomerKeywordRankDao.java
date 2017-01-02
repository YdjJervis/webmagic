package us.codecraft.webmagic.samples.amazon.dao.relation;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerKeywordRank;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/26 14:15
 */
@Repository
public interface CustomerKeywordRankDao extends BaseDao<CustomerKeywordRank> {
    List<CustomerKeywordRank> findByCustomer(String customerCode);
    List<CustomerKeywordRank> findNeedGenerateBatch();
    List<CustomerKeywordRank> findCustomerCodeIsOpen(String customerCode);
    CustomerKeywordRank findByObj(CustomerKeywordRank customerKeywordRank);
}
