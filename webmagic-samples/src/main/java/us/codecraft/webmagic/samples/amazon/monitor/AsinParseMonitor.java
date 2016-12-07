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
 * @Description: Asin对象转Url对象的监听器，会心跳监听ASIN表的变化
 * @date 2016/10/11
 */
@Service
public class AsinParseMonitor extends ParseMonitor {

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
        /* 查询未爬取 和 已经爬取首页的 */
        List<BatchAsin> batchAsinList = mBatchAsinService.findByStatus(0, 2);
        for (BatchAsin batchAsin : batchAsinList) {
            Site site = siteMap.get(batchAsin.siteCode);
            if (site == null) {
                site = mSiteService.find(batchAsin.siteCode);
                siteMap.put(batchAsin.siteCode, site);
            }

            /* 如果状态为0，表示未爬取首页，那就生成首页的Url */
            if (batchAsin.status == 0) {
                Url url = new Url();
                url.batchNum = batchAsin.batchNumber;
                url.url = siteMap.get(batchAsin.siteCode).basSite + "/dp/" + batchAsin.asin;
                url.urlMD5 = UrlUtils.md5(url.url);
                url.type = 3;
                url.siteCode = batchAsin.siteCode;
                url.asin = batchAsin.asin;

                urlList.add(url);

                batchAsin.status = 1;
                mBatchAsinService.update(batchAsin);
            } else {/* status == 2 */

                List<String> filterList = mAsinService.getUpdateFilters(batchAsin.star);
                /* 无论是全量爬取还是更新爬取，都生成相同的URL过滤器，然后生成URL */
                for (String filter : filterList) {
                    Url url = new Url();
                    url.batchNum = batchAsin.batchNumber;
                    url.url = siteMap.get(batchAsin.siteCode).basSite + "/" + Review.PRODUCT_REVIEWS + "/" + batchAsin.asin;
                    url.url = UrlUtils.setValue(url.url, "filterByStar", filter);
                    url.urlMD5 = UrlUtils.md5(url.url);
                    url.siteCode = batchAsin.siteCode;
                    url.asin = batchAsin.asin;
                    url.type = batchAsin.type;
                    urlList.add(url);
                }

                /* 装换成URL列表后，把爬取的状态改成爬取当中 */
                batchAsin.status = 1;
                mBatchAsinService.update(batchAsin);
            }

            Batch batch = mBatchService.findByBatchNumber(batchAsin.batchNumber);
            batch.status = 1;
            mBatchService.update(batch);
        }

        sLogger.info("转换后的URL列表如下：");
        sLogger.info(urlList);
        return urlList;
    }

}
