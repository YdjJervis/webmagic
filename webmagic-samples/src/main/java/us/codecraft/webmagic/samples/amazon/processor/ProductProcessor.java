package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.extractor.product.ProductExtractorAdapter;
import us.codecraft.webmagic.samples.amazon.pojo.*;
import us.codecraft.webmagic.samples.amazon.pojo.batch.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchAsin;
import us.codecraft.webmagic.samples.amazon.pojo.dict.Site;
import us.codecraft.webmagic.samples.amazon.pojo.relation.AsinRootAsin;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerAsin;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerProductInfo;
import us.codecraft.webmagic.samples.amazon.service.*;
import us.codecraft.webmagic.samples.amazon.service.batch.BatchService;
import us.codecraft.webmagic.samples.amazon.service.relation.AsinRootAsinService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerAsinService;
import us.codecraft.webmagic.samples.amazon.service.relation.CustomerProductInfoService;
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
    private ProductService mProductService;

    @Autowired
    private AsinRootAsinService mAsinRootAsinService;

    @Autowired
    private UrlHistoryService mHistoryService;

    @Autowired
    private CustomerAsinService mCustomerAsinService;

    @Autowired
    private BatchService mBatchService;

    @Autowired
    private CustomerProductInfoService mCustomerProductInfoService;

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
            asinRootAsin.siteCode = url.siteCode;
            asinRootAsin.rootAsin = rootAsin;
            mAsinRootAsinService.add(asinRootAsin);

            /* 改变批次详单 */
            BatchAsin dbBatchAsin = mBatchAsinService.findAllByAsin(url.batchNum, site.code, asinStr);
            dbBatchAsin.status = 2;
            dbBatchAsin.type = 1;
            mBatchAsinService.update(dbBatchAsin);

            /* 添加Asin到归档 */
            Asin asin = new Asin();
            asin.siteCode = site.code;
            asin.rootAsin = rootAsin;
            mAsinService.add(asin);

            /* 删除爬取的URL */
            mUrlService.deleteByUrlMd5(getUrl(page).urlMD5);
            /* 添加到历史表 */
            List<Url> urlList = new ArrayList<Url>();
            urlList.add(url);
            mHistoryService.addAll(urlList);

            /*三期业务 */
            Product product = new ProductExtractorAdapter().extract(site.code, asinRootAsin.rootAsin, page);
            mProductService.add(product);
            sLogger.info(product);

            /* 把客户和产品详细信息关系入库 */
            CustomerProductInfo productInfo = new CustomerProductInfo();
            productInfo.customerCode = getCustomerAsin(page).customerCode;
            productInfo.siteCode = asinRootAsin.siteCode;
            productInfo.asin = asinRootAsin.asin;
            productInfo.rootAsin = asinRootAsin.rootAsin;
            mCustomerProductInfoService.add(productInfo);


        }
    }

    @Override
    void dealPageNotFound(Page page) {
        super.dealPageNotFound(page);
        /* 把下架反应到客户和ASIN关系里面 */
        CustomerAsin customerAsin = getCustomerAsin(page);
        customerAsin.onSell = 0;
        mCustomerAsinService.update(customerAsin);
    }

    /**
     * 返回当前爬取URL对应的客户ASIN关系记录
     */
    private CustomerAsin getCustomerAsin(Page page) {
        Batch batch = mBatchService.findByBatchNumber(getUrl(page).batchNum);
        return mCustomerAsinService.find(new CustomerAsin(batch.customerCode, extractSite(page).code, extractAsin(page)));
    }

    @Override
    String extractAsin(Page page) {
        return page.getUrl().regex(".*/dp/([0-9A-Za-z]*)").get();
    }

    @Override
    public void execute() {
        sLogger.info("开始执行Root Asin爬取任务...");
        List<Url> urlList = mUrlService.find(R.CrawlType.REVIEW_MAIN_PAGE);
        startToCrawl(urlList);
    }
}