package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.BatchReview;
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
 * @Description: 对标记为监听的Review进行Url转换
 * @date 2016/10/11
 */
@Service
public class ReviewMonitor extends ParseMonitor {

    @Autowired
    private SiteService mSiteService;
    @Autowired
    private UrlService mUrlService;
    @Autowired
    private BatchReviewService mBatchReviewService;

    @Override
    public void execute() {
        List<Url> urlList = getUrl(true);

        /* 把所有需要监听的Review转换成URL后入库 */
        mUrlService.addAll(urlList);
    }

    @Override
    protected List<Url> getUrl(boolean isCrawlAll) {
        List<Url> urlList = new ArrayList<Url>();

        /* 把Review监控批次中没有转换成URL的转换成URL对象 */
        List<BatchReview> batchReviewList = mBatchReviewService.findNotCrawled();
        for (BatchReview batchReview : batchReviewList) {
            Url url = new Url();
            url.batchNum = batchReview.batchNumber;
            url.siteCode = batchReview.siteCode;
            Site site = mSiteService.find(batchReview.siteCode);
            url.url = site.site + "/gp/customer-reviews/" + batchReview.reviewID;
            url.urlMD5 = UrlUtils.md5(url.batchNum + url.url);
            url.type = batchReview.type;
            url.reviewId = batchReview.reviewID;
            url.priority = batchReview.priority;

            urlList.add(url);

            batchReview.status = 1;
            mBatchReviewService.update(batchReview);
        }

        sLogger.info("新添加的review的监听条数：" + urlList.size());
        return urlList;
    }
}
