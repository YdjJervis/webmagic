package com.eccang.spider.amazon.service.dict;

import com.eccang.spider.amazon.dao.dict.PayProfileDao;
import com.eccang.spider.amazon.pojo.dict.PayProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 收费基础配置 业务类
 * @date 2016/10/11
 */
@Service
public class PayProfileService {

    @Autowired
    private PayProfileDao mDao;

    public PayProfile findByCode(String businessCode){
        return mDao.findByCode(businessCode);
    }

    public void update(PayProfile payProfile){
        mDao.update(payProfile);
    }

}
