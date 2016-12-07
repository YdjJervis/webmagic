package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.RelationCustomerAsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.RelationCustomerAsin;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * 客户与asin关系功能服务
 * 2016/12/5 14:30
 */
@Service
public class RelationCustomerAsinService {
    @Autowired
    RelationCustomerAsinDao mRelationCustomerAsinDao;

    /**
     * 添加一条关系数据
     */
    public void add(RelationCustomerAsin relationCustomerAsin) {
        mRelationCustomerAsinDao.add(relationCustomerAsin);
    }

    /**
     * 批量添加
     */
    public void addAll(List<RelationCustomerAsin> relationCustomerAsinList) {
        mRelationCustomerAsinDao.addAll(relationCustomerAsinList);
    }

    /**
     * 通过id删除
     */
    public void deleteById(int id) {
        mRelationCustomerAsinDao.deleteById(id);
    }

    /**
     * 通过id更新
     */
    public void updateById(RelationCustomerAsin relationCustomerAsin) {
        mRelationCustomerAsinDao.updateById(relationCustomerAsin);
    }

    /**
     * 查询对应客户下的asin
     */
    public List<RelationCustomerAsin> findByCustomer(String customerCode) {
        return mRelationCustomerAsinDao.findByCustomer(customerCode);
    }

    /**
     *查询对应客户和asin是否存在
     */
    public RelationCustomerAsin findByCustomerAndAsin(String customerCode, String asin) {
        return mRelationCustomerAsinDao.findByCustomerAndAsin(customerCode, asin);
    }

    /**
     * 查询对应客户下是否存在这个asin
     */
    public boolean isExisted(String customerCode, String asin) {
        return mRelationCustomerAsinDao.findByCustomerAndAsin(customerCode, asin) != null;
    }
}