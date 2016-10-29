package us.codecraft.webmagic.samples.amazon.monitor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Review;
import us.codecraft.webmagic.samples.amazon.pojo.ReviewMonitor;
import us.codecraft.webmagic.samples.amazon.pojo.Site;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.ReviewMonitorService;
import us.codecraft.webmagic.samples.amazon.service.ReviewService;
import us.codecraft.webmagic.samples.amazon.service.SiteService;
import us.codecraft.webmagic.samples.amazon.service.UrlService;
import us.codecraft.webmagic.samples.base.monitor.ParseMonitor;
import us.codecraft.webmagic.samples.base.util.UrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 对标记为监听的Review进行Url转换
 * @date 2016/10/11
 */
@Service
public class ReviewIdMonitor extends ParseMonitor {

    @Autowired
    private ReviewMonitorService mMonitorService;
    @Autowired
    private ReviewService mReviewService;
    @Autowired
    private SiteService mSiteService;
    @Autowired
    private UrlService mUrlService;

    @Override
    public void execute() {
        List<Url> urlList = getUrl(true);
        mUrlService.addAll(urlList);
        if (CollectionUtils.isNotEmpty(urlList)) {
            for (Url url : urlList) {
                if (StringUtils.isNotEmpty(url.sauReviewId)) {
                    ReviewMonitor monitor = mMonitorService.findByReviewId(url.sauReviewId);
                    monitor.smrParsed = 1;
                    mMonitorService.update(monitor);
                }
            }
        }
    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {
        List<Url> urlList = new ArrayList<Url>();

        List<ReviewMonitor> monitorList = mMonitorService.findAll();
        if (CollectionUtils.isNotEmpty(monitorList)) {
            for (ReviewMonitor monitor : monitorList) {
                Url url = new Url();
                Review review = mReviewService.findByReviewId(monitor.smrReviewId);
                Site site = mSiteService.find(review.basCode);
                url.url = site.basSite + "/gp/customer-reviews/" + monitor.smrReviewId;
                url.urlMD5 = UrlUtils.md5(url.url);
                url.type = 1;
                url.siteCode = site.basCode;
                url.sauReviewId = monitor.smrReviewId;

                urlList.add(url);
            }
        }

        sLogger.info("新添加的review的监听条数：" + urlList.size());
        return urlList;
    }

}
