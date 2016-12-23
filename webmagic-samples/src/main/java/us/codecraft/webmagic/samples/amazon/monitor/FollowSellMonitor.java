package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Site;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.*;
import us.codecraft.webmagic.samples.base.monitor.ParseMonitor;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 对标记为监听的 跟卖 进行Url转换
 * @date 2016/12/2
 */
@Service
public class FollowSellMonitor extends ParseMonitor {

    @Autowired
    private SiteService mSiteService;

    @Autowired
    private UrlService mUrlService;

    @Autowired
    private FollowSellMonitorService mFollowSellMonitorService;

    @Override
    public void execute() {
        List<Url> urlList = getUrl(true);

        /* 把所有需要监听的Review转换成URL后入库 */
        mUrlService.addAll(urlList);

        /* 把所有已经转换成URL的监听Review的状态标记为已经转换 */
        for (Url url : urlList) {
            us.codecraft.webmagic.samples.amazon.pojo.FollowSellMonitor followSellMonitor = new us.codecraft.webmagic.samples.amazon.pojo.FollowSellMonitor(url.siteCode, url.asin);
            us.codecraft.webmagic.samples.amazon.pojo.FollowSellMonitor dbFollowSellMonitor = mFollowSellMonitorService.findByObject(followSellMonitor);
            dbFollowSellMonitor.parsed = 1;
            mFollowSellMonitorService.update(dbFollowSellMonitor);
        }

    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {
        List<Url> urlList = new ArrayList<Url>();

        List<us.codecraft.webmagic.samples.amazon.pojo.FollowSellMonitor> monitorList = mFollowSellMonitorService.findNotParsed();

        for (us.codecraft.webmagic.samples.amazon.pojo.FollowSellMonitor sell : monitorList) {
            Url url = new Url();
            // eg: https://www.amazon.com/gp/offer-listing/B0117RFOEG
            Site site = mSiteService.find(sell.siteCode);
            url.url = site.site + "/gp/offer-listing/" + sell.asin;
            url.urlMD5 = UrlUtils.md5(url.batchNum + url.url);
            url.type = 4;
            url.siteCode = site.code;
            url.asin = sell.asin;
            url.priority = sell.priority;

            urlList.add(url);
        }

        sLogger.info("新添加的 跟卖 的监听条数：" + urlList.size());
        return urlList;
    }

}
