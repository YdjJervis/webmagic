package us.codecraft.webmagic.samples.amazon.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.extractor.followsell.FollowSellExtractorAdapter;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.pojo.batch.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.FollowSell;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerFollowSell;
import us.codecraft.webmagic.samples.amazon.service.batch.BatchFollowSellService;
import us.codecraft.webmagic.samples.amazon.service.crawl.FollowSellService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerFollowSellService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 产品跟卖
 * @date 2016/11/4 17:02
 */
@Service
public class FollowSellProcessor extends BasePageProcessor implements ScheduledTask {

    @Autowired
    private FollowSellService mFollowSellService;
    @Autowired
    private CustomerFollowSellService mCustomerFollowSellService;
    @Autowired
    private BatchFollowSellService mBatchFollowSellService;

    @Override
    protected void dealOtherPage(Page page) {

        if (isFollowSellType(page)) {

            List<FollowSell> followSellList = new FollowSellExtractorAdapter().extract(extractSite(page).code, extractAsin(page), page);
            mFollowSellService.addAll(followSellList);

            /* 归档URL */
            mUrlService.deleteByUrlMd5(getUrl(page).urlMD5);
            mUrlHistoryService.add(getUrl(page));

            updateBatchStatus(page);
        }
    }

    /**
     * 更新：
     * 1，批次总单
     * 2，批次详单
     * 3，客户和跟卖关系
     * 4，归档URL
     */
    private void updateBatchStatus(Page page) {
        /* 更改详单记录状态 */
        mBatchFollowSellService.updateCrawlFinish(getUrl(page).batchNum, getUrl(page).siteCode, getUrl(page).asin);

        /* 更新批次总单的状态 */
        Batch batch = mBatchService.findByBatchNumber(getUrl(page).batchNum);
        Date currentTime = new Date();
        if (batch.startTime == null) {
            batch.startTime = currentTime;
        }
        float progress = mBatchFollowSellService.findAverageProgress(getUrl(page).batchNum);
        batch.progress = progress;

        if (progress == 1) {
            batch.finishTime = currentTime;
            batch.status = 2;

            /* 监控批次完成，把批次放进推送队列 */
            mPushQueueService.add(batch.number);
        } else {
            batch.status = 1;
        }
        mBatchService.update(batch);

        /* 更新客户-Review关系记录状态 */
        CustomerFollowSell customerFollowSell = mCustomerFollowSellService.find(batch.customerCode, getUrl(page).siteCode, getUrl(page).asin);
        customerFollowSell.times++;
        customerFollowSell.syncTime = currentTime;
        mCustomerFollowSellService.update(customerFollowSell);

        /* URL归档到历史表 */
        archiveCurrentUrl(page);
    }

    /**
     * @return 是否是跟卖类型URL
     */
    private boolean isFollowSellType(Page page) {
        return Pattern.compile(".*/gp/offer-listing/.*").matcher(page.getUrl().get()).matches();
    }

    @Override
    String extractAsin(Page page) {
        return page.getUrl().regex(".*/gp/offer-listing/([0-9A-Za-z]*)").get();
    }

    @Override
    public void execute() {
        sLogger.info("开始执行 跟卖 爬取任务...");
        List<Url> urlList = mUrlService.find(R.CrawlType.FOLLOW_SELL);
        startToCrawl(urlList);
    }
}