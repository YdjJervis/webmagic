package com.eccang.amazon.service.ebay;

import com.eccang.base.SpringTestCase;
import com.eccang.spider.ebay.pojo.EbayUrl;
import com.eccang.spider.ebay.service.EbayUrlService;
import com.eccang.spider.ebay.service.SellerInfoService;
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
        System.out.println(mSellerInfoService.isExistSeller("asdf"));
    }
}