package us.codecraft.webmagic.samples.amazon.dao.dict;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.dict.Customer;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户DAO
 * @date 2016/11/10 14:28
 */
@Repository
public interface CustomerDao extends BaseDao<Customer> {


}