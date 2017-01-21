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
        ebayUrl.url = "http://www.ebay.com/sch/i.html?_nkw=&_in_kw=1&_ex_kw=&_sacat=11116&_udlo=&_udhi=&LH_BIN=1&LH_ItemCondition=3&_ftrt=901&_ftrv=1&_sabdlo=&_sabdhi=&_samilow=&_samihi=&_sadis=15&_stpos=510000&_sargn=-1%26saslc%3D1&_salic=1&_sop=12&_dmd=1&_ipg=200";
        ebayUrl.urlMD5 = UrlUtils.md5(ebayUrl.url);
        ebayUrl.siteCode = "US";
        ebayUrl.type = 0;
        ebayUrl.categoryName = "Coins & Paper Money";
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