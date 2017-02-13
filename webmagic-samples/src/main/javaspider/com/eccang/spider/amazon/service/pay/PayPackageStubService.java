package com.eccang.spider.amazon.service.pay;

import com.eccang.spider.amazon.dao.pay.PayPackageStubDao;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<PayPackageStub> list = new ArrayList<>();

        for (PayPackageStub packageStub : payPackageStubList) {
            if (isExist(packageStub.stubCode)) {
                update(packageStub);
            } else {
                list.add(packageStub);
            }
        }

        if(CollectionUtils.isNotEmpty(list)){
            mDao.addAll(list);
        }
    }

    public void update(PayPackageStub payPackageStub) {
        mDao.update(payPackageStub);
    }

    public PayPackageStub findByCode(String code) {
        return mDao.findByCode(code);
    }

    public List<PayPackageStub> findByPayPackage(String payPackageCode){
        return mDao.findByPayPackage(payPackageCode);
    }

    public boolean isExist(String code) {
        return findByCode(code) != null;
    }

    public int findTotalPrice(String payPackageCode){
        return mDao.findTotalPrice(payPackageCode);
    }

}
