package com.eccang.spider.amazon.processor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.extractor.product.ProductExtractorAdapter;
import com.eccang.spider.amazon.pojo.Asin;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.pojo.crawl.Product;
import com.eccang.spider.amazon.pojo.dict.Site;
import com.eccang.spider.amazon.pojo.relation.AsinRootAsin;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.pojo.relation.CustomerProductInfo;
import com.eccang.spider.amazon.service.crawl.ProductService;
import com.eccang.spider.amazon.service.relation.AsinRootAsinService;
import com.eccang.spider.amazon.service.relation.CustomerAsinService;
import com.eccang.spider.amazon.service.relation.CustomerProductInfoService;
import com.eccang.spider.base.monitor.ScheduledTask;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 产品主页处理类
 * @date 2016/11/4 17:02
 */
@Service
public class ProductProcessor extends BasePageProcessor implements ScheduledTask {

    @Autowired
    private ProductService mProductService;

    @Autowired
    private AsinRootAsinService mAsinRootAsinService;

    @Autowired
    private CustomerAsinService mCustomerAsinService;

    @Autowired
    private CustomerProductInfoService mCustomerProductInfoService;

    @Override
    protected void dealOtherPage(Page page) {
        /* 如果是产品首页 */
        Url url = getUrl(page);
        Site site = extractSite(page);
        String asinStr = extractAsin(page);

        String rootAsin = page.getHtml().xpath("//li[@class='swatchAvailable']/@data-dp-url").regex("twister_([0-9a-zA-Z]*)").get();
        if (StringUtils.isEmpty(rootAsin)) {
            rootAsin = asinStr;
        }
        sLogger.info("提取出来的Root Asin ：" + rootAsin);

        /* 把Asin和RootAsin的关系连接上 */
        AsinRootAsin asinRootAsin = new AsinRootAsin();
        asinRootAsin.asin = asinStr;
        asinRootAsin.siteCode = url.siteCode;
        asinRootAsin.rootAsin = rootAsin;
        mAsinRootAsinService.add(asinRootAsin);

        /* 改变批次详单 */
        BatchAsin dbBatchAsin = mBatchAsinService.findAllByAsin(url.batchNum, site.code, asinStr);
        dbBatchAsin.status = 2;
        dbBatchAsin.type = 1;
        mBatchAsinService.update(dbBatchAsin);

        /* 添加Asin到归档 */
        Asin asin = new Asin();
        asin.siteCode = site.code;
        asin.rootAsin = rootAsin;
        mAsinService.add(asin);

        /* 删除爬取的URL */
        mUrlService.deleteByUrlMd5(getUrl(page).urlMD5);
        /* 添加到历史表 */
        List<Url> urlList = new ArrayList<Url>();
        urlList.add(url);
        mUrlHistoryService.addAll(urlList);

        /*三期业务 */
        Product product = new ProductExtractorAdapter().extract(site.code, asinRootAsin.rootAsin, page);
        if (product != null) {
            mProductService.add(product);
            sLogger.info(product);
        } else {
            sLogger.warn("当前站点未适配产品基本详细信息抓取：" + site.code);
        }

        /* 把客户和产品详细信息关系入库 */
        CustomerProductInfo productInfo = new CustomerProductInfo();
        productInfo.customerCode = getCustomerAsin(page).customerCode;
        productInfo.siteCode = asinRootAsin.siteCode;
        productInfo.asin = asinRootAsin.asin;
        productInfo.rootAsin = asinRootAsin.rootAsin;
        mCustomerProductInfoService.add(productInfo);

    }

    @Override
    void dealPageNotFound(Page page) {
        super.dealPageNotFound(page);
        /* 把下架反应到客户和ASIN关系里面 */
        CustomerAsin customerAsin = getCustomerAsin(page);
        customerAsin.onSell = 0;
        mCustomerAsinService.update(customerAsin);

        /* 有下架商品的时候也要同步进度 */
        mUrlService.asycBatchProgress(getUrl(page), new Date());
    }

    /**
     * 返回当前爬取URL对应的客户ASIN关系记录
     */
    private CustomerAsin getCustomerAsin(Page page) {
        Batch batch = mBatchService.findByBatchNumber(getUrl(page).batchNum);
        return mCustomerAsinService.find(new CustomerAsin(batch.customerCode, extractSite(page).code, extractAsin(page)));
    }

    @Override
    String extractAsin(Page page) {
        return page.getUrl().regex(".*/dp/([0-9A-Za-z]*)").get();
    }

    @Override
    public void execute() {
        sLogger.info("开始执行Root Asin爬取任务...");
        List<Url> urlList = mUrlService.find(R.CrawlType.REVIEW_MAIN_PAGE);
        startToCrawl(urlList);
    }
}