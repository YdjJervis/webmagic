package com.eccang.spider.amazon.service.pay;

import com.eccang.spider.amazon.dao.pay.PayPackageStubDao;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 收费套餐对应的每个业务的 业务类
 * @date 2016/10/11
 */
@Service
public class PayPackageStubService {

    @Autowired
    private PayPackageStubDao mDao;

    public void addAll(List<PayPackageStub> payPackageStubList) {
        mDao.addAll(payPackageStubList);
    }

    public List<PayPackageStub> findByStubCode(int code) {
        return mDao.findByStubCode(code);
    }

}
