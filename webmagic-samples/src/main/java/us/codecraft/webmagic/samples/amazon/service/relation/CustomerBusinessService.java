package us.codecraft.webmagic.samples.amazon.service.relation;

import com.eccang.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.relation.CustomerAsinDao;
import us.codecraft.webmagic.samples.amazon.dao.relation.CustomerBusinessDao;
import us.codecraft.webmagic.samples.amazon.dao.relation.CustomerKeywordRankDao;
import us.codecraft.webmagic.samples.amazon.dao.relation.CustomerReviewDao;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerAsin;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerBusiness;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerKeywordRank;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerReview;

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

    /**
     * 新增一条数据
     */
    public void addOne(CustomerBusiness customerBusiness) {
        mCustomerBusinessDao.addOne(customerBusiness);
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
    public void deleteOne(String customerCode, String businessCode) {
        mCustomerBusinessDao.deleteOne(customerCode, businessCode);
    }

    /**
     * 更新
     */
    public void updateOne(CustomerBusiness customerBusiness) {
        mCustomerBusinessDao.updateOne(customerBusiness);
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

    /**
     * 统计客户在对应业务功能下能使用的数据和已经使用的数据量，并更新对应业务功能下的使用数据量
     */
    public Map<String, Integer> getBusinessInfo(String customerCode, String businessCode) {

        Map<String, Integer> result = new HashMap<>();

        /* 查询客户下asin开启数 */
        int useNum;
        if (businessCode.equalsIgnoreCase(R.BusinessCode.ASIN_SPIDER)) {
            List<CustomerAsin> cusAList = mCustomerAsinDao.findByCustomerCodeIsOpen(customerCode);
            useNum = cusAList.size();
        } else if (businessCode.equalsIgnoreCase(R.BusinessCode.MONITOR_SPIDER)) {
            List<CustomerReview> cusRList = mCustomerReviewDao.findCustomerReviewIsOpen(customerCode);
            useNum = cusRList.size();
        } else if (businessCode.equalsIgnoreCase(R.BusinessCode.KEYWORD_RANK_SPIDER)) {
            List<CustomerKeywordRank> ckrList = mCustomerKeywordRankDao.findCustomerCodeIsOpen(customerCode);
            useNum = ckrList.size();
        } else {
            useNum = 0;
        }
        /* 查询客户对应下业务功能下的使用情况 */
        CustomerBusiness customerBusiness = mCustomerBusinessDao.findByCode(customerCode, businessCode);

        /*更新*/
        if (customerBusiness.getUseData() != useNum) {
            customerBusiness.setUseData(useNum);
            mCustomerBusinessDao.updateOne(customerBusiness);
        }

        result.put(R.BusinessInfo.USABLE_NUM, customerBusiness.getMaxData() - useNum);
        result.put(R.BusinessInfo.HASUSED_NUM, useNum);

        return result;
    }
}