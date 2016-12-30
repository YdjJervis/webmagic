package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.lang3.StringUtils;
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
import us.codecraft.webmagic.samples.base.util.UrlUtils;

import java.util.ArrayList;
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

    private static final String START_INDEX = "startIndex";

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

            /* 更新批次总单的状态 */
            Batch batch = mBatchService.findByBatchNumber(getUrl(page).batchNum);
            Date currentTime = new Date();
            if (batch.startTime == null) {
                batch.startTime = currentTime;
                batch.status = 1;
            }

            saveTurnPageUrl(page);

            List<Url> urlList = mUrlService.find(getUrl(page).batchNum, getUrl(page).siteCode, getUrl(page).asin, R.CrawlType.FOLLOW_SELL);
            int crawledNum = 0;
            for (Url url : urlList) {
                if (url.status == 200) {
                    ++crawledNum;
                }
            }
            if (urlList.size() == crawledNum) {
                /* 归档URL */
                mUrlService.deleteAll(urlList);
                mUrlHistoryService.addAll(urlList);

                /* 更改详单记录状态 */
                mBatchFollowSellService.updateCrawlFinish(getUrl(page).batchNum, getUrl(page).siteCode, getUrl(page).asin);

                batch.progress = mBatchFollowSellService.findAverageProgress(getUrl(page).batchNum);
                if (batch.progress == 1) {
                    batch.finishTime = currentTime;
                    batch.status = 2;

                    /* 监控批次完成，把批次放进推送队列 */
                    mPushQueueService.add(batch.number);
                }

            }
            mBatchService.update(batch);

            /* 更新客户-Review关系记录状态 */
            CustomerFollowSell customerFollowSell = mCustomerFollowSellService.find(batch.customerCode, getUrl(page).siteCode, getUrl(page).asin);
            customerFollowSell.times++;
            customerFollowSell.syncTime = currentTime;
            mCustomerFollowSellService.update(customerFollowSell);
        }
    }

    @Override
    void dealPageNotFound(Page page) {
        /* 把下架反应到客户和ASIN关系里面 */
        Batch batch = mBatchService.findByBatchNumber(getUrl(page).batchNum);
        CustomerFollowSell customerFollowSell = mCustomerFollowSellService.find(batch.customerCode, getUrl(page).siteCode, getUrl(page).asin);
        customerFollowSell.onSell = 0;
        mCustomerFollowSellService.update(customerFollowSell);

        /* 删除URL */
        mUrlService.deleteByUrlMd5(getUrl(page).urlMD5);
    }

    /**
     * 当为翻页第一页时，保存其它页的翻页URL到URL表
     */
    private void saveTurnPageUrl(Page page) {
        List<String> pageUrlList = extractPageUrlList(page);
        List<Url> urlList = new ArrayList<>();
        for (String urlStr : pageUrlList) {
            Url url = new Url();

            url.batchNum = getUrl(page).batchNum;
            url.siteCode = getUrl(page).siteCode;
            url.asin = getUrl(page).asin;

            url.type = getUrl(page).type;
            url.urlMD5 = UrlUtils.md5(url.batchNum + urlStr);
            url.url = urlStr;
            url.parentUrl = getUrl(page).url;
            url.priority = getUrl(page).priority;

            urlList.add(url);
        }
        mUrlService.addAll(urlList);
    }

    private int extractIndex(Page page) {
        String startIndex = UrlUtils.getValue(page.getUrl().get(), START_INDEX);
        if (StringUtils.isEmpty(startIndex)) {
            return 0;
        }
        return Integer.valueOf(startIndex);
    }

    /**
     * 如果是第一页就抽取页面
     */
    private List<String> extractPageUrlList(Page page) {
        if (extractIndex(page) == 0) {
            return page.getHtml().xpath("//ul[@class='a-pagination']//a/@href").regex("(.*page_[0-9]+.*)").all();
        }
        return new ArrayList<>();
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