package com.eccang.spider.amazon.service.relation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.relation.CustomerFollowSellDao;
import com.eccang.spider.amazon.pojo.relation.CustomerFollowSell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户-跟卖关系业务
 * @date 2016/10/11
 */
@Service
public class CustomerFollowSellService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private CustomerFollowSellDao mDao;

    public void add(CustomerFollowSell customerFollowSell) {
        if (!isExist(customerFollowSell.customerCode, customerFollowSell.siteCode, customerFollowSell.asin)) {
            mDao.add(customerFollowSell);
        } else {
            mDao.update(customerFollowSell);
        }
    }

    public void addAll(List<CustomerFollowSell> customerFollowSellList) {
        List<CustomerFollowSell> newList = new ArrayList<>();
        for (CustomerFollowSell customerFollowSell : customerFollowSellList) {
            if (!isExist(customerFollowSell.customerCode, customerFollowSell.siteCode, customerFollowSell.asin)) {
                newList.add(customerFollowSell);
            } else {
                mDao.update(customerFollowSell);
            }
        }
        if (CollectionUtils.isNotEmpty(newList)) {
            mDao.addAll(newList);
        }
    }

    public CustomerFollowSell find(String customerCode, String siteCode, String asin) {
        return mDao.find(customerCode, siteCode, asin);
    }

    public boolean isExist(String customerCode, String siteCode, String asin) {
        return find(customerCode, siteCode, asin) != null;
    }

    public List<CustomerFollowSell> findNeedGenerateBatch() {
        return mDao.findNeedGenerateBatch();
    }

    public void update(CustomerFollowSell customerFollowSell) {
        mDao.update(customerFollowSell);
    }

    public List<CustomerFollowSell> findByCustomer(String customer) {
        return mDao.findByCustomer(customer);
    }
}
