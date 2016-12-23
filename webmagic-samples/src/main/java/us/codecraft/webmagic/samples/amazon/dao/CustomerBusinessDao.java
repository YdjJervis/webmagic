package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.CustomerBusiness;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/15 16:32
 */
@Repository
public interface CustomerBusinessDao {

    void addOne(CustomerBusiness customerBusiness);

    void addAll(List<CustomerBusiness> customerBusinessList);

    void deleteOne(String customerCode, String businessCode);

    void updateOne(CustomerBusiness customerBusiness);

    List<CustomerBusiness> findAll();

    List<CustomerBusiness> findByCustomerCode(String customerCode);

    CustomerBusiness findByCode(String customerCode, String businessCode);
}
