package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.CustomerAsin;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 客户和ASIN的关系DAO
 * @date 2016/11/10 14:28
 */
@Repository
public interface CustomerAsinDao extends BaseDao<CustomerAsin> {

    List<CustomerAsin> findByCustomerCode(String customerCode);

    CustomerAsin findByObject(CustomerAsin customerAsin);

    List<CustomerAsin> findNeedGenerateBatch();

    List<CustomerAsin> findByCustomerCodeIsOpen(String customerCode);
}