package com.eccang.spider.amazon.processor;

import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.extractor.followsell.FollowSellExtractorAdapter;
import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchFollowSell;
import com.eccang.spider.amazon.pojo.crawl.FollowSell;
import com.eccang.spider.amazon.pojo.relation.CustomerFollowSell;
import com.eccang.spider.amazon.service.batch.BatchFollowSellService;
import com.eccang.spider.amazon.service.crawl.FollowSellService;
import com.eccang.spider.amazon.service.relation.CustomerFollowSellService;
import com.eccang.spider.base.monitor.ScheduledTask;
import com.eccang.spider.base.util.UrlUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;

import java.util.*;
import java.util.regex.Matcher;
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

        List<FollowSell> followSellList = new FollowSellExtractorAdapter().extract(extractSite(page).code, getUrl(page).asin, page);
        for (FollowSell followSell : followSellList) {
            followSell.batchNum = getUrl(page).batchNum;
        }

        sLogger.info(followSellList);
        mFollowSellService.addAll(followSellList);

        /* 更新批次总单的状态 */
        Batch batch = mBatchService.findByBatchNumber(getUrl(page).batchNum);
        CustomerFollowSell customerFollowSell = mCustomerFollowSellService.find(batch.customerCode, getUrl(page).siteCode, getUrl(page).asin);

        Set<String> lastSellerIdSet = new Gson().fromJson(customerFollowSell.extra, new TypeToken<Set>() {
        }.getType());
        if (lastSellerIdSet == null) {
            lastSellerIdSet = new HashSet<>();
        }

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
            BatchFollowSell batchFollowSell = mBatchFollowSellService.find(getUrl(page).batchNum, getUrl(page).siteCode, getUrl(page).asin);
            batchFollowSell.status = 2;

            /* 关系表大字段保存最后爬取时当前ASIN所有的SellerID */
            Set<String> sellerSet = new HashSet<>();
            FollowSell followSell = new FollowSell();
            followSell.batchNum = batch.number;
            followSell.siteCode = customerFollowSell.siteCode;
            followSell.asin = getUrl(page).asin;
            for (FollowSell fs : mFollowSellService.findAll(followSell)) {
                sellerSet.add(fs.sellerID);
            }
            customerFollowSell.extra = new Gson().toJson(sellerSet);

            boolean changed = false;
            if (sellerSet.size() != lastSellerIdSet.size()) {
                changed = true;
            } else {
                for (String sellerId : sellerSet) {
                    if (!lastSellerIdSet.contains(sellerId)) {
                        changed = true;
                        break;
                    }
                }
            }
            batchFollowSell.isChanged = changed ? 1 : 0;
            mBatchFollowSellService.update(batchFollowSell);

            asyncBatchAndPush(page, batch, false);
        }
        mBatchService.update(batch);

        /* 更新客户-Review关系记录状态 */
        customerFollowSell.times++;
        customerFollowSell.syncTime = currentTime;
        mCustomerFollowSellService.update(customerFollowSell);

    }

    @Override
    void dealPageNotFound(Page page) {
        /* 把下架反应到客户和ASIN关系里面 */
        Batch batch = mBatchService.findByBatchNumber(getUrl(page).batchNum);
        CustomerFollowSell customerFollowSell = mCustomerFollowSellService.find(batch.customerCode, getUrl(page).siteCode, getUrl(page).asin);
        customerFollowSell.onSell = 0;
        mCustomerFollowSellService.update(customerFollowSell);

        /* 跟新批次详单状态 */
        BatchFollowSell batchFollowSell = mBatchFollowSellService.find(batch.number, getUrl(page).siteCode, getUrl(page).asin);
        batchFollowSell.status = 2;
        mBatchFollowSellService.update(batchFollowSell);

        asyncBatchAndPush(page, batch, true);

        /* 归档URL */
        mUrlService.deleteByUrlMd5(getUrl(page).urlMD5);
        mUrlHistoryService.add(getUrl(page));
    }


    /**
     * 1，同步总进度；
     * 2，总进度为1时推送数据
     */
    private void asyncBatchAndPush(Page page, Batch batch, boolean update) {
        batch.progress = mBatchFollowSellService.findAverageProgress(getUrl(page).batchNum);
        if (batch.progress == 1) {
            batch.finishTime = new Date();
            batch.status = 2;
                /* 监控批次完成，把批次放进推送队列 */
            mPushQueueService.add(batch.number);
        }
        if (update) {
            mBatchService.update(batch);
        }

    }

    /**
     * 当为翻页第一页时，保存其它页的翻页URL到URL表
     */
    private void saveTurnPageUrl(Page page) {

        int maxPageNum = extractMaxPageNum(page);
        if (extractIndex(page) == 0 && maxPageNum > 1) {
            sLogger.info("当前ASIN的跟卖最大页码：" + maxPageNum);
            List<Url> urlList = new ArrayList<>();
            for (int i = 2; i <= maxPageNum; i++) {
                Url url = new Url();

                String urlStr = page.getUrl().get() + "/ref=olp_page_" + i + "?startIndex=" + (i - 1) * 10 + "&overridePriceSuppression=1";

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
    }

    /**
     * 根据当前页面URL抽取页码
     */
    private int extractIndex(Page page) {
        Matcher matcher = Pattern.compile(".*page_([0-9]+).*").matcher(page.getUrl().get());
        if (matcher.matches()) {
            return Integer.valueOf(matcher.group(1));
        }
        return 0;
    }

    private int extractMaxPageNum(Page page) {
        int maxPage = 0;
        List<String> pageNumList = page.getHtml().xpath("//ul[@class='a-pagination']//a/@href").regex(".*page_([0-9]+).*").all();
        for (String pageNum : pageNumList) {
            if (Integer.valueOf(pageNum) > maxPage) {
                maxPage = Integer.valueOf(pageNum);
            }
        }
        return maxPage;
    }

    @Override
    public void execute() {
        sLogger.info("开始执行 跟卖 爬取任务...");
        List<Url> urlList = mUrlService.find(R.CrawlType.FOLLOW_SELL);
        startToCrawl(urlList);
    }

    public static void main(String[] args) {
        Matcher matcher = Pattern.compile(".*page_([0-9]+).*").matcher("https://www.amazon.com/gp/offer-listing");
        if (matcher.matches()) {
            System.out.println(matcher.group(1));
        }
    }
}