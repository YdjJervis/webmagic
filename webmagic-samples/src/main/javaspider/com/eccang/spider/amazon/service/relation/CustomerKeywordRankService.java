package com.eccang.spider.amazon.service.relation;

import com.eccang.spider.amazon.dao.relation.CustomerKeywordRankDao;
import com.eccang.spider.amazon.pojo.relation.CustomerKeywordRank;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<CustomerKeywordRank> list = new ArrayList<>();
        for (CustomerKeywordRank rank : customerKeywordRanks) {
            if (isExist(rank)) {
                update(rank);
            } else {
                list.add(rank);
            }
        }
        if (CollectionUtils.isNotEmpty(list)) {
            mCustomerKeywordRankDao.addAll(list);
        }
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

    public void deleteById(int id) {
        mCustomerKeywordRankDao.delete(id);
    }

    public boolean isExist(CustomerKeywordRank customerKeywordRank) {
        return mCustomerKeywordRankDao.findByObj(customerKeywordRank) != null;
    }
}