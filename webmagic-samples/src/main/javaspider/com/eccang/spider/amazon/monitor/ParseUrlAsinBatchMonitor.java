package com.eccang.spider.amazon.monitor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.pojo.crawl.Review;
import com.eccang.spider.amazon.pojo.dict.Customer;
import com.eccang.spider.amazon.pojo.dict.Site;
import com.eccang.spider.amazon.service.AsinService;
import com.eccang.spider.amazon.service.UrlService;
import com.eccang.spider.amazon.service.batch.BatchAsinService;
import com.eccang.spider.amazon.service.batch.BatchService;
import com.eccang.spider.amazon.service.dict.CustomerService;
import com.eccang.spider.amazon.service.dict.SiteService;
import com.eccang.spider.base.monitor.ParseMonitor;
import com.eccang.spider.base.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 批次转换Review全量爬取和更新爬取的URL
 * @date 2016/10/11
 */
@Service
public class ParseUrlAsinBatchMonitor extends ParseMonitor {

    private final static Logger mLogger = LoggerFactory.getLogger(R.BusinessLog.AS);
    @Autowired
    protected UrlService mUrlService;
    @Autowired
    protected AsinService mAsinService;

    @Autowired
    private BatchService mBatchService;

    @Autowired
    private BatchAsinService mBatchAsinService;

    @Autowired
    private SiteService mSiteService;

    @Override
    public void execute() {
        mLogger.info("开始批次转URL...");
        List<Url> urlList = getUrl(true);
        mUrlService.addAll(urlList);
        mLogger.info("结束批次转URL。");
    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {
        mLogger.info("获取一批需要转URL的详单...");
        List<Url> urlList = new ArrayList<Url>();

        mLogger.info("转换成首页爬取URL...");
        /* 把没有爬取首页的详单转换成爬取首页的URL，并改变爬取类型为爬取首页 */
        List<BatchAsin> batchAsinList = mBatchAsinService.findNotCrawledMainPage();
        mLogger.info("没有爬取首页的个数：" + batchAsinList.size());

        for (BatchAsin batchAsin : batchAsinList) {

            Batch batch = mBatchService.findByBatchNumber(batchAsin.batchNumber);
            Site site = mSiteService.find(batchAsin.siteCode);
            Customer customer = mCustomerService.findByCode(batch.customerCode);

            if (customer.debug) {
                mLogger.debug("客户" + batch.customerCode + "批次详单:" + batchAsin);
            }

            Url url = new Url();
            url.url = site.site + "/dp/" + batchAsin.asin;
            initUrl(batchAsin, url);

            if (customer.debug) {
                mLogger.debug("客户" + batch.customerCode + "生成首页URL:" + url);
            }

            /* 装换成URL列表后，把爬取的状态改成爬取当中 */
            batchAsin.status = 1;
            mBatchAsinService.update(batchAsin);
            if (customer.debug) {
                mLogger.debug("客户" + batch.customerCode + "更新详单状态:" + batchAsin);
            }

            batch.status = 1;
            mBatchService.update(batch);

            if (customer.debug) {
                mLogger.debug("客户" + batch.customerCode + "更新总单状态" + batch);
            }

            urlList.add(url);
        }

        mLogger.info("转换成全量爬取URL...");
        /* 把需要全量爬取的批次详单转换成全量爬取的URL，并改变爬取类型为全量爬取 */
        batchAsinList = mBatchAsinService.findNotCrawledReview();
        mLogger.info("没有全量爬取的个数：" + batchAsinList.size());
        for (BatchAsin batchAsin : batchAsinList) {

            Batch batch = mBatchService.findByBatchNumber(batchAsin.batchNumber);
            Site site = mSiteService.find(batchAsin.siteCode);
            Customer customer = mCustomerService.findByCode(batch.customerCode);

            List<String> filterList = mAsinService.getUpdateFilters(batchAsin.star);
            for (String filter : filterList) {
                Url url = new Url();
                url.url = site.site + "/" + Review.PRODUCT_REVIEWS + "/" + batchAsin.asin;
                url.url = UrlUtils.setValue(url.url, "filterByStar", filter);
                initUrl(batchAsin, url);

                if (customer.debug) {
                    mLogger.debug("客户" + batch.customerCode + "生成Reivew全量爬取URL:" + url);
                }
                urlList.add(url);
            }

            /* 装换成URL列表后，把爬取的状态改成爬取当中 */
            batchAsin.status = 3;
            mBatchAsinService.update(batchAsin);
            if(customer.debug){
                mLogger.debug("客户" + batch.customerCode + "更新详单状态:" + batchAsin);
            }

            batch.status = 1;
            mBatchService.update(batch);
            if (customer.debug) {
                mLogger.debug("客户" + batch.customerCode + "更新总单状态" + batch);
            }
        }

        mLogger.info("转换成更新爬取URL...");
        /* 把需要更新爬取的爬取的批次详单转换更新爬取的URL，并改变爬取类型为更新爬取 */
        batchAsinList = mBatchAsinService.findNotUpdatedReview();
        mLogger.info("没有更新爬取的个数：" + batchAsinList.size());
        for (BatchAsin batchAsin : batchAsinList) {

            Batch batch = mBatchService.findByBatchNumber(batchAsin.batchNumber);
            Site site = mSiteService.find(batchAsin.siteCode);
            Customer customer = mCustomerService.findByCode(batch.customerCode);

            List<String> filterList = mAsinService.getUpdateFilters(batchAsin.star);
            for (String filter : filterList) {
                Url url = new Url();
                url.url = site.site + "/" + Review.PRODUCT_REVIEWS + "/" + batchAsin.asin;
                url.url = UrlUtils.setValue(url.url, "filterByStar", filter);
                initUrl(batchAsin, url);

                if (customer.debug) {
                    mLogger.debug("客户" + batch.customerCode + "生成Reivew更新爬取URL:" + url);
                }
                urlList.add(url);
            }
            batchAsin.status = 5;
            mBatchAsinService.update(batchAsin);
            if(customer.debug){
                mLogger.debug("客户" + batch.customerCode + "更新详单状态:" + batchAsin);
            }

            batch.status = 1;
            mBatchService.update(batch);
            if (customer.debug) {
                mLogger.debug("客户" + batch.customerCode + "更新总单状态" + batch);
            }
        }

        mLogger.info("转换URL结束。");
        return urlList;
    }

    private void initUrl(BatchAsin batchAsin, Url url) {
        url.batchNum = batchAsin.batchNumber;
        url.urlMD5 = UrlUtils.md5(batchAsin.batchNumber + url.url);
        url.siteCode = batchAsin.siteCode;
        url.asin = batchAsin.asin;
        url.type = batchAsin.type;
    }

}
