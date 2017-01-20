package com.eccang.spider.ebay.processor;

import com.eccang.spider.base.service.UserAgentService;
import com.eccang.spider.downloader.EbayHttpClientImplDownloader;
import com.eccang.spider.ebay.pojo.EbayUrl;
import com.eccang.spider.ebay.service.EbayUrlService;
import com.eccang.spider.ebay.service.SellerInfoService;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;
import java.util.Set;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/17 9:07
 */
@Service
public abstract class EbayProcessor implements PageProcessor {

    private Site mSite = Site.me().setRetryTimes(3).setSleepTime(10 * 1000).setTimeOut(10 * 1000);
    private Set<Integer> mDealSet = Sets.newHashSet(0, 200, 402, 403, 404, 407, 417, 429, 503);
    public static final String URL_EXTRA = "url_extra";
    Logger sLogger = Logger.getLogger(getClass());

    @Autowired
    UserAgentService mUserAgentService;
    @Autowired
    EbayUrlService mEbayUrlService;
    @Autowired
    SellerInfoService mSellerInfoService;
    @Autowired
    EbayHttpClientImplDownloader mEbayHttpClientImplDownloader;

    @Override
    public void process(Page page) {
        updateUrlStatus(page);
        if(page.getStatusCode() == 200) {
            dealOtherPage(page);
        }
    }

    @Override
    public Site getSite() {
        sLogger.info("getSite()::");
        mSite.setUserAgent(mUserAgentService.findRandomUA().userAgent).setAcceptStatCode(mDealSet);
        return mSite;
    }

    /**
     * @param page 更新Url爬取状态,成功或失败
     */
    private void updateUrlStatus(Page page) {
        EbayUrl url = getUrl(page);

        int statusCode = page.getStatusCode();
        sLogger.info("当前页面:" + page.getUrl() + " 爬取状态：" + statusCode);

        url.status = statusCode;

        url.crawling = 0;
        sLogger.info("改变状态后的Url对象：" + url);

        mEbayUrlService.update(url);
    }

    protected EbayUrl getUrl(Page page) {
        return (EbayUrl) page.getRequest().getExtra(URL_EXTRA);
    }

    /**
     * 处理其它页码，对亚马逊页面爬取的扩展
     */
    protected abstract void dealOtherPage(Page page);

    void startToCrawl(List<EbayUrl> urlList) {
        sLogger.info("找到状态码不为200的Url个数：" + urlList.size());
        if (CollectionUtils.isNotEmpty(urlList)) {

            Spider mSpider = Spider.create(this)
                    .setDownloader(mEbayHttpClientImplDownloader)
                    .thread(100);

            for (EbayUrl url : urlList) {
                Request request = new Request(url.url);
                request.putExtra(URL_EXTRA, url);
                mSpider.addRequest(request);

                url.crawling = 1;
                mEbayUrlService.update(url);
            }

            sLogger.info("开始爬取卖家信息...");
            mSpider.start();
        }
    }
}