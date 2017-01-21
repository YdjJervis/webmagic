package com.eccang.spider.ebay.service;

import com.eccang.spider.ebay.dao.EbayUrlHistoryDao;
import com.eccang.spider.ebay.pojo.EbayUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/21 14:52
 */
@Service
public class EbayUrlHistoryService {
    @Autowired
    EbayUrlHistoryDao mDao;

    public void add(EbayUrl ebayUrl) {
        if(mDao.findCount(ebayUrl.urlMD5) < 1) {
            mDao.add(ebayUrl);
        }
    }
}