package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.*;
import us.codecraft.webmagic.samples.amazon.service.*;
import us.codecraft.webmagic.samples.base.monitor.ParseMonitor;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

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
public class BatchParseUrlMonitor extends ParseMonitor {

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
        List<Url> urlList = getUrl(true);
        mUrlService.addAll(urlList);
    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {

        List<Url> urlList = new ArrayList<Url>();

        Map<String, Site> siteMap = new HashMap<String, Site>();

        /* 把没有爬取首页的详单转换成爬取首页的URL，并改变爬取类型为爬取首页 */
        List<BatchAsin> batchAsinList = mBatchAsinService.findNotCrawledMainPage();
        for (BatchAsin batchAsin : batchAsinList) {

            Site site = siteMap.get(batchAsin.siteCode);
            if (site == null) {
                site = mSiteService.find(batchAsin.siteCode);
                siteMap.put(batchAsin.siteCode, site);
            }

            Url url = new Url();
            url.url = siteMap.get(batchAsin.siteCode).basSite + "/dp/" + batchAsin.asin;
            initUrl(batchAsin, url);

            /* 装换成URL列表后，把爬取的状态改成爬取当中 */
            batchAsin.status = 1;
            mBatchAsinService.update(batchAsin);

            Batch batch = mBatchService.findByBatchNumber(batchAsin.batchNumber);
            batch.status = 1;
            mBatchService.update(batch);
        }

        /* 把需要全量爬取的批次详单转换成全量爬取的URL，并改变爬取类型为全量爬取 */
        batchAsinList = mBatchAsinService.findNotCrawledReview();
        for (BatchAsin batchAsin : batchAsinList) {

            Site site = siteMap.get(batchAsin.siteCode);
            if (site == null) {
                site = mSiteService.find(batchAsin.siteCode);
                siteMap.put(batchAsin.siteCode, site);
            }

            List<String> filterList = mAsinService.getUpdateFilters(batchAsin.star);
            for (String filter : filterList) {
                Url url = new Url();
                url.url = siteMap.get(batchAsin.siteCode).basSite + "/" + Review.PRODUCT_REVIEWS + "/" + batchAsin.asin;
                url.url = UrlUtils.setValue(url.url, "filterByStar", filter);
                initUrl(batchAsin, url);
                urlList.add(url);
            }

            /* 装换成URL列表后，把爬取的状态改成爬取当中 */
            batchAsin.status = 3;
            mBatchAsinService.update(batchAsin);

            Batch batch = mBatchService.findByBatchNumber(batchAsin.batchNumber);
            batch.status = 1;
            mBatchService.update(batch);
        }

        /* 把需要更新爬取的爬取的批次详单转换更新爬取的URL，并改变爬取类型为更新爬取 */
        batchAsinList = mBatchAsinService.findNotUpdatedReview();
        for (BatchAsin batchAsin : batchAsinList) {

            Site site = siteMap.get(batchAsin.siteCode);
            if (site == null) {
                site = mSiteService.find(batchAsin.siteCode);
                siteMap.put(batchAsin.siteCode, site);
            }

            List<String> filterList = mAsinService.getUpdateFilters(batchAsin.star);
            for (String filter : filterList) {
                Url url = new Url();
                url.url = siteMap.get(batchAsin.siteCode).basSite + "/" + Review.PRODUCT_REVIEWS + "/" + batchAsin.asin;
                url.url = UrlUtils.setValue(url.url, "filterByStar", filter);
                initUrl(batchAsin, url);
                urlList.add(url);
            }
            batchAsin.status = 5;
            mBatchAsinService.update(batchAsin);

            Batch batch = mBatchService.findByBatchNumber(batchAsin.batchNumber);
            batch.status = 1;
            mBatchService.update(batch);
        }

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
