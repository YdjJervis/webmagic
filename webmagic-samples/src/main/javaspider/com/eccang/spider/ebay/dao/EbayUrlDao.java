package com.eccang.spider.ebay.dao;

import com.eccang.spider.ebay.pojo.EbayUrl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/16 16:30
 */
@Repository
public interface EbayUrlDao {

    void addAll(List<EbayUrl> urls);

    void add(EbayUrl ebayUrl);

    void update(EbayUrl url);

    List<EbayUrl> findCategoryUrl(int limit);

    List<EbayUrl> findProductUrl(int limit);

    void deleteById(EbayUrl ebayUrl);

    List<EbayUrl> findProductListing();

    int findProductUrlCount(String urlMD5);
}
