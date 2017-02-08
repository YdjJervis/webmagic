package com.eccang.spider.amazon.service.pay;

import com.eccang.spider.amazon.dao.pay.PayPackageDao;
import com.eccang.spider.amazon.pojo.pay.PayPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 收费套餐 业务类
 * @date 2016/10/11
 */
@Service
public class PayPackageService {

    @Autowired
    private PayPackageDao mDao;

    public void add(PayPackage payPackage) {
        mDao.add(payPackage);
    }

    public List<PayPackage> findByPayPackageCode(String payPackageCode) {
        return mDao.findByPayPackageCode(payPackageCode);
    }

    public PayPackage findByCode(String payPackageCode, String stubCode) {
        return mDao.findByCode(payPackageCode, stubCode);
    }

    public void update(PayPackage payPackage) {
        mDao.update(payPackage);
    }

}
