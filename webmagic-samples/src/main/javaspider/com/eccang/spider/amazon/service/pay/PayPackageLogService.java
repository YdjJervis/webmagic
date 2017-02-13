package com.eccang.spider.amazon.service.pay;

import com.eccang.spider.amazon.dao.pay.PayPackageLogDao;
import com.eccang.spider.amazon.pojo.pay.PayPackageLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 套餐操作业务
 * @date 2017/2/13 16:59
 */
@Service
public class PayPackageLogService {

    @Autowired
    private PayPackageLogDao mDao;

    public void add(PayPackageLog payPackageLog){
        mDao.add(payPackageLog);
    }

    public List<PayPackageLog> findByCusCode(String customerCode){
        return mDao.findByCusCode(customerCode);
    }
}

