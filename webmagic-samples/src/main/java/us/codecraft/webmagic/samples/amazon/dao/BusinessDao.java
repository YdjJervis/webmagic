package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Business;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/15 16:04
 */
@Repository
public interface BusinessDao {

    void addOne(Business business);

    void addAll(List<Business> businessList);

    void delete(String businessCode);

    void update(Business business);

    List<Business> findAll();

    Business findByCode(String businessCode);
}
