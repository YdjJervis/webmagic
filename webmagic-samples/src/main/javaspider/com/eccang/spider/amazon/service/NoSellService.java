package com.eccang.spider.amazon.service;

import com.eccang.spider.amazon.dao.NoSellDao;
import com.eccang.spider.amazon.pojo.Asin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 下架商品 业务
 * @date 2016/12/7
 */
@Service
public class NoSellService {

    @Autowired
    private NoSellDao mDao;

    public void add(Asin asin) {
        if (!isExist(asin)) {
            mDao.add(asin);
        }
    }

    public boolean isExist(Asin asin) {
        return mDao.findExist(asin.siteCode, asin.rootAsin) > 0;
    }

}
