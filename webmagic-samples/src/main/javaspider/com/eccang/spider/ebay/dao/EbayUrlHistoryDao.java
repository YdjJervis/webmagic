package com.eccang.spider.ebay.dao;

import com.eccang.spider.ebay.pojo.EbayUrl;
import org.springframework.stereotype.Repository;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/21 14:45
 */
@Repository
public interface EbayUrlHistoryDao {

    void add(EbayUrl ebayUrl);

    int findCount(String urlMD5);
}