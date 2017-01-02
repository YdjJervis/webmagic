package com.eccang.spider.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.batch.BatchFollowSell;
import com.eccang.spider.amazon.pojo.dict.Site;
import com.eccang.spider.amazon.service.UrlService;
import com.eccang.spider.amazon.service.batch.BatchFollowSellService;
import com.eccang.spider.amazon.service.dict.SiteService;
import com.eccang.spider.base.monitor.ParseMonitor;
import com.eccang.spider.base.util.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 对标记为监听的 跟卖 进行Url转换
 * @date 2016/12/2
 */
@Service
public class ParseUrlFollowSellMonitor extends ParseMonitor {

    @Autowired
    private SiteService mSiteService;

    @Autowired
    private UrlService mUrlService;

    @Autowired
    private BatchFollowSellService mBatchFollowSellService;

    @Override
    public void execute() {
        List<Url> urlList = getUrl(true);

        /* 把所有需要监听的Review转换成URL后入库 */
        mUrlService.addAll(urlList);
    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {
        List<Url> urlList = new ArrayList<>();

        List<BatchFollowSell> batchFollowSellList = mBatchFollowSellService.findNotCrawled();

        for (BatchFollowSell batchFollowSell : batchFollowSellList) {
            Url url = new Url();
            url.batchNum = batchFollowSell.batchNumber;
            // eg: https://www.amazon.com/gp/offer-listing/B0117RFOEG
            Site site = mSiteService.find(batchFollowSell.siteCode);
            url.url = site.site + "/gp/offer-listing/" + batchFollowSell.asin;
            url.urlMD5 = UrlUtils.md5(url.batchNum + url.url);
            url.type = batchFollowSell.type;
            url.siteCode = site.code;
            url.asin = batchFollowSell.asin;
            url.priority = batchFollowSell.priority;
            urlList.add(url);

            /* 跟新爬取状态为爬取中 */
            batchFollowSell.status = 1;
            mBatchFollowSellService.update(batchFollowSell);
        }
        sLogger.info("新添加的 跟卖 的监听条数：" + urlList.size());

        return urlList;
    }

}
