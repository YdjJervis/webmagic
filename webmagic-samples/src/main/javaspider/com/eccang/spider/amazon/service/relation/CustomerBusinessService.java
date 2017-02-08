package com.eccang.spider.amazon.service.relation;

import com.eccang.R;
import com.eccang.spider.amazon.dao.relation.CustomerAsinDao;
import com.eccang.spider.amazon.dao.relation.CustomerBusinessDao;
import com.eccang.spider.amazon.dao.relation.CustomerKeywordRankDao;
import com.eccang.spider.amazon.dao.relation.CustomerReviewDao;
import com.eccang.spider.amazon.pojo.relation.CustomerBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/15 16:52
 */
@Service
public class CustomerBusinessService {
    @Autowired
    CustomerBusinessDao mCustomerBusinessDao;

    @Autowired
    CustomerAsinDao mCustomerAsinDao;

    @Autowired
    CustomerReviewDao mCustomerReviewDao;

    @Autowired
    CustomerKeywordRankDao mCustomerKeywordRankDao;

    @Autowired
    private CustomerFollowSellService mCustomerFollowSellService;

    /**
     * 新增一条数据
     */
    public void add(CustomerBusiness customerBusiness) {
        if (isExist(customerBusiness.customerCode, customerBusiness.businessCode)) {
            update(customerBusiness);
        } else {
            mCustomerBusinessDao.add(customerBusiness);
        }
    }

    /**
     * 批量新增
     */
    public void addAll(List<CustomerBusiness> customerBusinessList) {
        mCustomerBusinessDao.addAll(customerBusinessList);
    }

    /**
     * 删除
     */
    public void delete(String customerCode, String businessCode) {
        mCustomerBusinessDao.delete(customerCode, businessCode);
    }

    /**
     * 更新
     */
    public void update(CustomerBusiness customerBusiness) {
        mCustomerBusinessDao.update(customerBusiness);
    }

    /**
     * 批量查询
     */
    public List<CustomerBusiness> findAll() {
        return mCustomerBusinessDao.findAll();
    }

    /**
     * 查询客户下的业务
     */
    public List<CustomerBusiness> findByCustomerCode(String customerCode) {
        return mCustomerBusinessDao.findByCustomerCode(customerCode);
    }

    /**
     * 查询客户下的某个业务
     */
    public CustomerBusiness findByCode(String customerCode, String businessCode) {
        return mCustomerBusinessDao.findByCode(customerCode, businessCode);
    }

    public boolean isExist(String customerCode, String businessCode) {
        return findByCode(customerCode, businessCode) != null;
    }

    /**
     * 统计客户在对应业务功能下能使用的数据和已经使用的数据量，并更新对应业务功能下的使用数据量
     */
    public Map<String, Integer> getBusinessInfo(String customerCode, String businessCode) {

        Map<String, Integer> result = new HashMap<>();

        /* 查询客户下asin开启数 */
        int useNum;
        if (businessCode.equalsIgnoreCase(R.BusinessCode.ASIN_SPIDER)) {
            useNum = mCustomerAsinDao.findUsedCount(customerCode);
        } else if (businessCode.equalsIgnoreCase(R.BusinessCode.MONITOR_SPIDER)) {
            useNum = mCustomerReviewDao.findUsedCount(customerCode);
        } else if (businessCode.equalsIgnoreCase(R.BusinessCode.KEYWORD_RANK_SPIDER)) {
            useNum = mCustomerKeywordRankDao.findUsedCount(customerCode);
        } else if (businessCode.equalsIgnoreCase(R.BusinessCode.FOLLOW_SELL)) {
            useNum = mCustomerFollowSellService.findUsedCount(customerCode);
        } else {
            useNum = 0;
        }
        /* 查询客户对应下业务功能下的使用情况 */
        CustomerBusiness customerBusiness = mCustomerBusinessDao.findByCode(customerCode, businessCode);

        /*更新*/
        if (customerBusiness.useData != useNum) {
            customerBusiness.useData = useNum;
            mCustomerBusinessDao.update(customerBusiness);
        }

        result.put(R.BusinessInfo.USABLE_NUM, customerBusiness.maxData - useNum);
        result.put(R.BusinessInfo.HAS_USED_NUM, useNum);

        return result;
    }
}