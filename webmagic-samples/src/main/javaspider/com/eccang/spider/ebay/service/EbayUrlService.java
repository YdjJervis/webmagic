package com.eccang.spider.ebay.service;

import com.eccang.spider.ebay.dao.EbayUrlDao;
import com.eccang.spider.ebay.pojo.EbayUrl;
import org.apache.log4j.Logger;
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

    Logger sLogger = Logger.getLogger(getClass());

    @Autowired
    EbayUrlDao mDao;

    public void addAll(List<EbayUrl> urls) {
        mDao.addAll(urls);
    }

    public void add(EbayUrl ebayUrl) {
        if(mDao.findProductUrlCount(ebayUrl.urlMD5) < 1) {
            mDao.add(ebayUrl);
        } else {
            sLogger.info("url has existed. " + ebayUrl.toString());
        }
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

    public void deleteById(EbayUrl ebayUrl) {
        mDao.deleteById(ebayUrl);
    }

    public List<EbayUrl> findProductListing() {
        return mDao.findProductListing();
    }

    public boolean isExist(String urlMD5) {
        return mDao.findProductUrlCount(urlMD5) > 0;
    }
}