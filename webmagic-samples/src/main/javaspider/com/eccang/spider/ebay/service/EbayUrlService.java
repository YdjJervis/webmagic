package com.eccang.spider.ebay.service;

import com.eccang.spider.ebay.dao.EbayUrlDao;
import com.eccang.spider.ebay.pojo.EbayUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/16 16:37
 */
@Service
public class EbayUrlService {

    @Autowired
    EbayUrlDao mDao;

    public void addAll(List<EbayUrl> urls) {
        mDao.addAll(urls);
    }

    public void update(EbayUrl url) {
        mDao.update(url);
    }

    public List<EbayUrl> findCategoryUrl(int limit) {
        return mDao.findCategoryUrl(limit);
    }

    public List<EbayUrl> findProductUrl(int limit) {
        return mDao.findProductUrl(limit);
    }
}