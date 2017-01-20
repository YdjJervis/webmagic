package com.eccang.amazon.service.ebay;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.base.util.UrlUtils;
import com.eccang.spider.ebay.pojo.EbayUrl;
import com.eccang.spider.ebay.service.EbayUrlService;
import com.eccang.spider.ebay.service.SellerInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/17 11:46
 */
public class EbayUrlServiceTest extends SpringTestCase{
    @Autowired
    EbayUrlService mService;
    @Autowired
    SellerInfoService mSellerInfoService;

    @Test
    public void addAll() {
        List<EbayUrl> ebayUrls = new ArrayList<>();
        EbayUrl ebayUrl = new EbayUrl();
        ebayUrl.url = "http://www.ebay.com/sch/ebayadvsearch";
        ebayUrl.type = 2;
        ebayUrl.categoryName = "all";
        ebayUrls.add(ebayUrl);
        mService.addAll(ebayUrls);
    }

    @Test
    public void update() {
        EbayUrl ebayUrl = new EbayUrl();
        ebayUrl.url = "http://www.ebay.com/sch/ebayadvsearch";
        ebayUrl.type = 2;
        ebayUrl.categoryName = "all";
        mService.update(ebayUrl);
    }

    @Test
    public void find() {
        List<EbayUrl> ebayUrls = mService.findCategoryUrl(10);
    }

    @Test
    public void isExisted(){
        System.out.println(mSellerInfoService.isExistSeller("asdf", "US"));
    }

    @Test
    public void deleteById() {
        List<EbayUrl> categoryUrl = mService.findCategoryUrl(15);
        while(CollectionUtils.isNotEmpty(categoryUrl)) {
            for (EbayUrl ebayUrl : categoryUrl) {
                String indexStr = UrlUtils.getValue(ebayUrl.url, "_pgn");
                if(StringUtils.isNotEmpty(indexStr) && Integer.valueOf(indexStr) > 49) {
                    System.out.println(ebayUrl);
                    mService.deleteById(ebayUrl);
                }
            }
            categoryUrl = mService.findCategoryUrl(15);
        }
    }
}