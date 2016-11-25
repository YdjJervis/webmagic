package us.codecraft.webmagic.samples.amazon.processor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.BatchAsin;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.BatchAsinService;
import us.codecraft.webmagic.samples.base.monitor.ScheduledTask;

import java.util.Date;
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

    @Override
    protected void dealOtherPage(Page page) {
        /* 如果是产品首页 */
        if (Pattern.compile(".*/dp/.*").matcher(page.getUrl().get()).matches()) {
            String asinStr = extractAsin(page);
            Asin asin = mAsinService.findByAsin(asinStr);

            String rootAsinUrl = page.getHtml().xpath("//link[@rel='canonical']/@href").get();
            int index = rootAsinUrl.lastIndexOf("/");
            String rootAsin = rootAsinUrl.substring(index + 1, rootAsinUrl.length());

            if (StringUtils.isEmpty(rootAsin)) {
                sLogger.warn("未提取到Root Asin：" + page.getUrl().get());
            } else {
                sLogger.info("提取出来的Root Asin ：" + rootAsin);

                /* 如果该rootAsin已经存在，那么久把该asin记录修改为
                1，已经转换成了全量爬取URL状态，
                2，不需要更新爬取状态 */

                if (mAsinService.haveSameRootAsin(rootAsin)) {
                    mAsinService.setParsedNotUpdate(asin);

                    /* 二期业务：把所有根节点相同的ASIN的状态改变一下 */
                    List<BatchAsin> batchAsinList = mBatchAsinService.findAllByAsin(asinStr);
                    for (BatchAsin batchAsin : batchAsinList) {
                        batchAsin.rootAsin = rootAsin;
                        batchAsin.progress = 1;
                        batchAsin.type = 3;
                        batchAsin.extra = "SRA";
                        batchAsin.startTime = batchAsin.finishTime = new Date();
                    }
                    mBatchAsinService.updateAll(batchAsinList);
                }

                asin.saaRootAsin = rootAsin;

                /* root asin已经找到了 */
                asin.saaCrawledHead = 2;

                /* 找到了根ASIN了，更新asin状态 */
                mAsinService.update(asin);

                /* 删除爬取的URL */
                mUrlService.deleteByAsin(asin.saaAsin);

            }
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