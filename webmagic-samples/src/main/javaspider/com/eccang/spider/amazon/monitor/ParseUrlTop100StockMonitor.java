package com.eccang.spider.amazon.monitor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.dict.Site;
import com.eccang.spider.amazon.pojo.top100.SellingProduct;
import com.eccang.spider.amazon.pojo.top100.StockUrl;
import com.eccang.spider.amazon.service.dict.SiteService;
import com.eccang.spider.amazon.service.top100.SellingProductService;
import com.eccang.spider.amazon.service.top100.StockUrlService;
import com.eccang.spider.base.monitor.ParseMonitor;
import com.eccang.spider.base.util.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/2/22 11:25
 */
@Service
public class ParseUrlTop100StockMonitor extends ParseMonitor {

    @Autowired
    private SellingProductService mSellingProductService;
    @Autowired
    private SiteService mSiteService;
    @Autowired
    private StockUrlService mService;

    @Override
    public void execute() {
        List<StockUrl> stockUrls = getStockUrl();
        mService.addAll(stockUrls);
    }

    private List<StockUrl> getStockUrl() {
        List<StockUrl> stockUrls = new ArrayList<>();

        List<SellingProduct> products = mSellingProductService.findNeedParseStockProduct(1);
        StockUrl stockUrl;
        ;
        for (SellingProduct product : products) {

            stockUrl = new StockUrl();
            Site site = mSiteService.find(product.siteCode);

            stockUrl.batchNum = product.batchNum;
            stockUrl.siteCode = product.siteCode;
            stockUrl.asin = product.asin;
            stockUrl.type = R.StockCrawlUrlType.PRODUCT_URL;
            stockUrl.url = product.url;
            stockUrl.urlMD5 = UrlUtils.md5(stockUrl.url);
            stockUrl.pUrl = product.url;
            stockUrls.add(stockUrl);

            product.status = R.StockCrawlStatus.CONVERT_TO_URL;
            mSellingProductService.updateByObj(product);
        }

        return stockUrls;
    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {
        return null;
    }
}