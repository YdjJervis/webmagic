package us.codecraft.webmagic.samples.amazon.service.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.relation.CustomerRankKeywordDao;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerRankKeyword;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/26 15:23
 */
@Service
public class CustomerRankKeywordService {

    @Autowired
    CustomerRankKeywordDao mCustomerRankKeywordDao;

    public void add(CustomerRankKeyword customerRankKeyword) {
        mCustomerRankKeywordDao.add(customerRankKeyword);
    }

    public void addAll(List<CustomerRankKeyword> customerRankKeywords){
        mCustomerRankKeywordDao.addAll(customerRankKeywords);
    }

    public List<CustomerRankKeyword> findByCustomer(String customerCode) {
        return mCustomerRankKeywordDao.findByCustomer(customerCode);
    }

    public void update(CustomerRankKeyword customerRankKeyword) {
        mCustomerRankKeywordDao.update(customerRankKeyword);
    }

    public List<CustomerRankKeyword> findNeedGenerateBatch() {
        return mCustomerRankKeywordDao.findNeedGenerateBatch();
    }
}