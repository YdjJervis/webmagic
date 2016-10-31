package us.codecraft.webmagic.downloader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.processor.BasePageProcessor;
import us.codecraft.webmagic.samples.amazon.service.UrlService;

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

    Logger mLogger = Logger.getLogger(getClass());

    @Override
    protected void onError(Request request) {
        Url url = (Url) request.getExtra(BasePageProcessor.URL_EXTRA);
        url.sauCrawling = 0;
        mLogger.warn("HttpClient下载异常，更新URL状态：" + url);
        mUrlService.update(url);
    }
}