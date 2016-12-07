package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.*;
import us.codecraft.webmagic.samples.amazon.service.*;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 产品主页处理类
 * @date 2016/11/4 17:02
 */
@Service
public class ProductProcessor extends BasePageProcessor implements ScheduledTask {

    @Autowired
    private BatchAsinService mBatchAsinService;

    @Autowired
    private ProductService mProductService;

    @Autowired
    private AsinRootAsinService mAsinRootAsinService;

    @Autowired
    private UrlHistoryService mHistoryService;

    @Autowired
    private AsinService mAsinService;

    @Override
    protected void dealOtherPage(Page page) {
        /* 如果是产品首页 */
        if (Pattern.compile(".*/dp/.*").matcher(page.getUrl().get()).matches()) {
            Url url = getUrl(page);
            Site site = extractSite(page);
            String asinStr = extractAsin(page);

            String rootAsin = page.getHtml().xpath("//li[@class='swatchAvailable']/@data-dp-url").regex("twister_([0-9a-zA-Z]*)").get();
            if (StringUtils.isEmpty(rootAsin)) {
                rootAsin = asinStr;
            }
            sLogger.info("提取出来的Root Asin ：" + rootAsin);

            /* 把Asin和RootAsin的关系连接上 */
            AsinRootAsin asinRootAsin = new AsinRootAsin();
            asinRootAsin.asin = asinStr;
            asinRootAsin.rootAsin = rootAsin;
            mAsinRootAsinService.add(asinRootAsin);

            /* 改变批次详单 */
            BatchAsin dbBatchAsin = mBatchAsinService.findAllByAsin(url.batchNum, site.basCode, asinStr);
            dbBatchAsin.status = 2;
            mBatchAsinService.update(dbBatchAsin);

            /* 添加Asin到归档 */
            Asin asin = new Asin();
            asin.siteCode = site.basCode;
            asin.rootAsin = rootAsin;
            mAsinService.add(asin);

            /**
             * 如果该rootAsin已经存在，那么久把该asin记录做如下修改：
             * 1，已经转换成了全量爬取URL状态，
             * 2，不需要更新爬取状态
             */
            /*if (mAsinService.haveSameRootAsin(rootAsin)) {
                mAsinService.setParsedNotUpdate(asin);

                *//* 二期业务：把所有根节点相同的ASIN的状态改变一下 *//*
                List<BatchAsin> batchAsinList = mBatchAsinService.findAllByAsin(getUrl(page).batchNum, site.basCode, asinStr);
                for (BatchAsin batchAsin : batchAsinList) {
                    batchAsin.rootAsin = rootAsin;
                    batchAsin.progress = 1;
                    batchAsin.type = 3;
                    batchAsin.extra = "SRA";
                    batchAsin.startTime = batchAsin.finishTime = new Date();
                }
                mBatchAsinService.updateAll(batchAsinList);
            }*/

            /* 删除爬取的URL */
            mUrlService.deleteOne(url.batchNum, site.basCode, asinStr);
            /* 添加到历史表 */
            List<Url> urlList = new ArrayList<Url>();
            urlList.add(url);
            mHistoryService.addAll(urlList);

            /* 三期业务 */
            /*Product product = new ProductExtractor(extractSite(page).basCode, rootAsin, page).extract();
            mProductService.add(product);
            sLogger.info(product);*/

        }
    }

    @Override
    String extractAsin(Page page) {
        return page.getUrl().regex(".*/dp/([0-9A-Za-z]*)").get();
    }

    @Override
    public void execute() {
        sLogger.info("开始执行Root Asin爬取任务...");
        List<Url> urlList = mUrlService.find(3);
        startToCrawl(urlList);
    }
}