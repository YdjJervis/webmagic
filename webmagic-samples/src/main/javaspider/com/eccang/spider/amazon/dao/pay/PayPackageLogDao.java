package com.eccang.spider.amazon.dao.pay;

import com.eccang.spider.amazon.pojo.pay.PayPackageLog;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 套餐操作日志表 Dao层
 * @date 2016/10/11 18:00
 */
@Repository
public interface PayPackageLogDao extends BaseDao<PayPackageLog> {

    /**
     * @param customerCode 客户码
     * @return 客户所有操作日志
     */
    List<PayPackageLog> findByCusCode(String customerCode);

}
