package com.eccang.spider.amazon.dao.pay;

import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 套餐类型表，一个套餐码有多个业务 Dao层
 * @date 2016/10/11 18:00
 */
@Repository
public interface PayPackageStubDao extends BaseDao<PayPackageStub> {

    List<PayPackageStub> findByPayPackage(String payPackageCode);
}
