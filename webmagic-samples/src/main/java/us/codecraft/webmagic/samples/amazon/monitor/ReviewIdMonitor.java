package us.codecraft.webmagic.samples.amazon.monitor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.*;
import us.codecraft.webmagic.samples.amazon.service.*;
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
    @Autowired

    @Override
    public void execute() {
        List<Url> urlList = getUrl(true);

        /* 把所有需要监听的Review转换成URL后入库 */
        mUrlService.addAll(urlList);

        /* 把所有已经转换成URL的监听Review的状态标记为已经转换 */
        for (Url url : urlList) {
            if (StringUtils.isNotEmpty(url.reviewId)) {
                ReviewMonitor monitor = mMonitorService.findByReviewId(url.reviewId);
                monitor.smrParsed = 1;
                mMonitorService.update(monitor);
            }
        }
    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {
        List<Url> urlList = new ArrayList<Url>();

            List<ReviewMonitor> monitorList = mMonitorService.findAll();
        for (ReviewMonitor monitor : monitorList) {
            Url url = new Url();
            Review review = mReviewService.findByReviewId(monitor.smrReviewId);

            if (review == null) {
                continue;
            }

            Site site = mSiteService.find(review.siteCode);
            url.url = site.basSite + "/gp/customer-reviews/" + monitor.smrReviewId;
            url.urlMD5 = UrlUtils.md5(url.batchNum + url.url);
            url.type = 1;
            url.siteCode = site.basCode;
            url.reviewId = monitor.smrReviewId;
            url.priority = monitor.smrPriority;

            urlList.add(url);
        }

        sLogger.info("新添加的review的监听条数：" + urlList.size());
        return urlList;
    }
}
