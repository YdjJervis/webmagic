package com.eccang.spider.amazon.monitor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.batch.BatchTop100;
import com.eccang.spider.amazon.pojo.dict.Site;
import com.eccang.spider.amazon.service.UrlService;
import com.eccang.spider.amazon.service.batch.BatchTop100Service;
import com.eccang.spider.amazon.service.dict.SiteService;
import com.eccang.spider.base.monitor.ParseMonitor;
import com.eccang.spider.base.util.UrlUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/2/15 10:36
 */
@Service
public class ParseUrlTop100BatchMonitor extends ParseMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(R.BusinessLog.TOP);

    @Autowired
    UrlService mUrlService;
    @Autowired
    BatchTop100Service mBatchTop100Service;
    @Autowired
    SiteService mSiteService;

    @Override
    public void execute() {
        List<Url> urls = getUrl(true);

        /*把所有需要监听的Top100商品解析转换成URL后入库*/
        mUrlService.addAll(urls);
    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {

        List<Url> urls = new ArrayList<>();

        /*查询Top100批次关系表中需要转换URL的数据转换成URL对象*/
        List<BatchTop100> batchTop100s = mBatchTop100Service.findNotCrawl();

        Url url;
        for (BatchTop100 batchTop100 : batchTop100s) {
            url = new Url();
            url.batchNum = batchTop100.batchNum;
            url.siteCode = batchTop100.siteCode;
            url.priority = 4;
            url.url = getTop100HomeUrl(batchTop100.siteCode);
            url.urlMD5 = UrlUtils.md5(batchTop100.batchNum + url.url);
            url.type = R.CrawlType.TOP_100_DEPARTMENT;

            batchTop100.status = 1;
            mBatchTop100Service.update(batchTop100);
            urls.add(url);
        }

        LOGGER.info("新添加的Top100商品信息爬取的监听条数：{}", urls.size());

        return urls;
    }

    private String getTop100HomeUrl(String siteCode) {

        String url = null;
        Site site = mSiteService.find(siteCode);
        StringBuilder sb = new StringBuilder(site.site);
        if(StringUtils.isNotEmpty(site.site)) {
            url = sb.append(R.Top100.TOP100_BEST_SELLERS).toString();
        } else {
            LOGGER.info("站点码{}在基础表中不存在.", siteCode);
        }
        return url;
    }
}