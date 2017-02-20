package com.eccang.spider.downloader;

import com.eccang.spider.amazon.service.batch.BatchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.processor.BasePageProcessor;
import com.eccang.spider.amazon.service.UrlService;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/29 18:56
 */
@Service
public class HttpClientImplDownloader extends HttpClientDownloader {

    @Autowired
    private UrlService mUrlService;
    @Autowired
    private BatchService mBatchService;

    Logger mLogger = Logger.getLogger(getClass());

    @Override
    protected void onError(Request request) {
        Url url = (Url) request.getExtra(BasePageProcessor.URL_EXTRA);
        url.crawling = 0;
        url.status = 0;
        mLogger.warn("HttpClient下载异常，更新URL状态：" + url);
        mUrlService.update(url);

        mLogger.warn("HttpClient下载异常，更新总单(" + url.batchNum + ")解析url的总次数");
        mBatchService.updateTimes(url.batchNum, true);
    }
}