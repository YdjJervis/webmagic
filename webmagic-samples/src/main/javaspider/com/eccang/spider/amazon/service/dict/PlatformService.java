package com.eccang.spider.amazon.service.dict;

import com.eccang.spider.amazon.dao.dict.PlatformDao;
import com.eccang.spider.amazon.pojo.dict.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 平台 业务
 * @date 2016/10/11
 */
@Service
public class PlatformService {

    @Autowired
    private PlatformDao mPlatformDao;

    public Platform findByCode(String platformCode) {
        return mPlatformDao.findByCode(platformCode);
    }


}
