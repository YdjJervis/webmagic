package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.RelationCustomerAsin;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * 2016/12/5 14:02
 */
@Repository
public interface RelationCustomerAsinDao {

    void add(RelationCustomerAsin relationCustomerAsin);

    void addAll(List<RelationCustomerAsin> relationCustomerAsinList);

    void deleteById(int id);

    void updateById(RelationCustomerAsin relationCustomerAsin);

    List<RelationCustomerAsin> findByCustomer(String customerCode);

    RelationCustomerAsin findByCustomerAndAsin(String customerCode, String asin);

}
