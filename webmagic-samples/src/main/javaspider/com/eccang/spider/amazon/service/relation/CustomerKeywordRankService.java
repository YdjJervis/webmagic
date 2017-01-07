package com.eccang.spider.amazon.service.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.relation.CustomerKeywordRankDao;
import com.eccang.spider.amazon.pojo.relation.CustomerKeywordRank;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/26 15:23
 */
@Service
public class CustomerKeywordRankService {

    @Autowired
    CustomerKeywordRankDao mCustomerKeywordRankDao;

    public void add(CustomerKeywordRank customerKeywordRank) {
        mCustomerKeywordRankDao.add(customerKeywordRank);
    }

    public void addAll(List<CustomerKeywordRank> customerKeywordRanks) {
        mCustomerKeywordRankDao.addAll(customerKeywordRanks);
    }

    public List<CustomerKeywordRank> findByCustomer(String customerCode) {
        return mCustomerKeywordRankDao.findByCustomer(customerCode);
    }

    public void update(CustomerKeywordRank customerKeywordRank) {
        mCustomerKeywordRankDao.update(customerKeywordRank);
    }

    public List<CustomerKeywordRank> findNeedGenerateBatch() {
        return mCustomerKeywordRankDao.findNeedGenerateBatch();
    }

    public int findUsedCount(String customerCode) {
        return mCustomerKeywordRankDao.findUsedCount(customerCode);
    }

    public CustomerKeywordRank findByObj(CustomerKeywordRank customerKeywordRank) {
        return mCustomerKeywordRankDao.findByObj(customerKeywordRank);
    }

    public boolean isExist(CustomerKeywordRank customerKeywordRank) {
        return mCustomerKeywordRankDao.findByObj(customerKeywordRank) != null;
    }
}