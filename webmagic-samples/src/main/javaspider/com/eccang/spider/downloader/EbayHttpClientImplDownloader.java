package com.eccang.spider.downloader;

import com.eccang.spider.amazon.processor.BasePageProcessor;
import com.eccang.spider.ebay.pojo.EbayUrl;
import com.eccang.spider.ebay.service.EbayUrlService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/29 18:56
 */
@Service
public class EbayHttpClientImplDownloader extends HttpClientDownloader {

    @Autowired
    private EbayUrlService mEbayUrlService;

    Logger mLogger = Logger.getLogger(getClass());

    @Override
    protected void onError(Request request) {
        EbayUrl ebayUrl = (EbayUrl) request.getExtra(BasePageProcessor.URL_EXTRA);
        ebayUrl.crawling = 0;
        ebayUrl.status = 0;
        mLogger.warn("HttpClient下载异常，更新URL状态：" + ebayUrl);
        mEbayUrlService.update(ebayUrl);
    }
}