package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.AsinService;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.base.monitor.ParseMonitor;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void execute() {
        List<Url> urlList = getUrl(true);
        mUrlService.addAll(urlList);

        sLogger.info("更新ASIN状态...更新数量：" + urlList.size());
        for (Url url : urlList) {
            if (url.asin != null) {
                mAsinService.updateStatus(url.asin, false);
            }
        }
    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {

        List<Asin> asinList;
        if (isCrawlAll) {
            asinList = mAsinService.findAll();
        } else {
            asinList = mAsinService.findCrawledAll();
        }
        sLogger.info("剩余未转换成URL的Asin数量：" + asinList.size());

        List<Url> urlList = new ArrayList<Url>();

        for (Asin asin : asinList) {

            /* 如果当前站点设置了不可爬取，直接进入下一个循环 */
            if (asin.site.basCrawl != 1) {
                continue;
            }

            List<String> filterList;

            /* 更新爬取和全量爬取的过滤器生成策略是不同的 */
            if (isCrawlAll) {
                filterList = mAsinService.getFilterWords(asin.saaStar);
            } else {
                filterList = mAsinService.getUpdateFilters(asin.saaStar);
            }
            sLogger.debug("过滤器列表：" + filterList);

            for (String filter : filterList) {

                Url url = new Url();
                url.url = asin.site.basSite + "/" + Review.PRODUCT_REVIEWS + "/" + asin.saaAsin;
                /* 为Url添加过滤器 */
                url.url = UrlUtils.setValue(url.url, "filterByStar", filter);
                url.urlMD5 = UrlUtils.md5(url.url);
                url.siteCode = asin.site.basCode;
                url.asin = asin;
                url.priority = asin.saaPriority;
                url.type = 0;

                /* 如果是更新爬取，他的url类型为2 */
                if (!isCrawlAll) {
                    url.type = 2;
                }

                url.saaAsin = asin.saaAsin;

                urlList.add(url);
            }
        }

        sLogger.info("转换后的URL列表如下：");
        sLogger.info(urlList);
        return urlList;
    }

}
