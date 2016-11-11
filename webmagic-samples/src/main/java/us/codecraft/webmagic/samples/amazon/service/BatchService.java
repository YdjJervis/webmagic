package us.codecraft.webmagic.samples.amazon.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.BatchDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Batch;
import us.codecraft.webmagic.samples.amazon.pojo.BatchAsin;
import us.codecraft.webmagic.samples.amazon.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户 业务
 * @date 2016/10/11
 */
@Service
public class BatchService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private BatchDao mBatchDao;

    @Autowired
    private AsinService mAsinService;

    @Autowired
    private BatchAsinService mBatchAsinService;

    @Autowired
    private UrlService mUrlService;

    /**
     * 根据ASIN列表生成批次单号，批次详单，和转换成ASIN表
     * 记录
     * @param customerCode 客户码
     * @param asinList     ASIN 列表
     */
    public void add(String customerCode, List<Asin> asinList) {
        mLogger.info("开始处理客户的订单：customerCode = " + customerCode);
        Batch batch = generate(customerCode, asinList);

        if (batch != null) {

            /* 把已经爬取过的和未爬取过的ASIN分开 */
            List<Asin> crawledList = new ArrayList<Asin>();
            List<Asin> newList = new ArrayList<Asin>();

            for (Asin asin : asinList) {
                if (mAsinService.isExist(asin.saaAsin)) {
                    crawledList.add(asin);
                } else {
                    newList.add(asin);
                }
            }
            mLogger.info("ASIN列表大小：新的 " + newList.size() + " 已经爬取的 " + crawledList.size());
            /* 生成批次单详情列表，已经爬取过的和未爬取的做不同的处理 */
            List<BatchAsin> baList = new ArrayList<BatchAsin>();

            mLogger.info("对已经爬取过的列表插入到批次单明细前面...");
            for (Asin asin : crawledList) {

                Asin byAsin = mAsinService.findByAsin(asin.saaAsin);

                BatchAsin ba = initBatchAsin(batch, asin);
                ba.crawled = 1;
                ba.progress = byAsin.saaProgress;
                /* 已经在爬取的ASIN的进度 < 1就标记为全量爬取，否则标记为更新爬取 */
                ba.type = ba.progress < 1 ? 0 : 1;

                baList.add(ba);
            }

            mLogger.info("新的批次单明细插入到列表后面...");
            for (Asin asin : newList) {

                BatchAsin ba = initBatchAsin(batch, asin);
                baList.add(ba);
            }

            mLogger.info("批次单入库：" + batch);
            add(batch);

            mLogger.info("批次详单入库：" + baList);
            mBatchAsinService.addAll(baList);

            /*
             * 添加ASIN批次到ASIN表中。
             * 步骤1，如果一部分不存在ASIN表中，则直接把列表转成ASIN表中的记录；
             * 步骤2，如果一部分存在，就根据ASIN的爬取状态更新ASIN的优先级或URL的爬取优先级；
             */
            //步骤1
            List<Asin> parsedAsinList = new ArrayList<Asin>();
            mLogger.info("把新的ASIN批次转换成ASIN表记录：" + newList);
            for (Asin asin : newList) {
                Asin parsedAsin = new Asin();
                parsedAsin.saaAsin = asin.saaAsin;
                parsedAsin.saaPriority = asin.saaPriority;
                parsedAsin.saaStar = asin.saaStar;
                parsedAsin.asinSource = asin.asinSource;
                parsedAsin.site = asin.site;
                parsedAsin.asinSource = asin.asinSource;
                parsedAsinList.add(asin);
            }
            mAsinService.addAll(parsedAsinList);

            //步骤2
            mLogger.info("已经爬取过的ASIN更改ASIN和URL表状态：" + crawledList);
            for (Asin asin : crawledList) {
                Asin parsedAsin = mAsinService.findByAsin(asin.saaAsin);
                /* 如果新的优先级大于原先的优先级，就更改爬取优先级 */
                if (asin.saaPriority > parsedAsin.saaPriority) {
                    parsedAsin.saaPriority = asin.saaPriority;

                    /* 如果已经转换成URL了，那么还得把所有URL的优先级提升一下 */
                    if (parsedAsin.saaParsed == 1) {
                        mUrlService.updatePriority(parsedAsin.saaAsin, parsedAsin.saaPriority);
                    }

                    mAsinService.update(parsedAsin);
                }

            }
        }
    }

    public void add(Batch batch) {
        mBatchDao.add(batch);
    }

    private BatchAsin initBatchAsin(Batch batch, Asin asin) {
        BatchAsin ba = new BatchAsin();
        ba.siteCode = asin.site.basCode;
        ba.batchNumber = batch.number;
        ba.asin = asin.saaAsin;
        ba.rootAsin = asin.saaRootAsin;
        return ba;
    }

    private Batch generate(String customerCode, List<Asin> asinList) {
        if (asinList == null) {
            mLogger.warn("asin list is null");
            return null;
        }

        Batch batch = new Batch();
        /* result like "EC20161110164710555" */
        batch.number = "EC" + DateUtils.format(new Date(), DateUtils.FORMAT_BATCH);
        batch.customerCode = customerCode;
        batch.type = 0;

        mLogger.info("客户 " + customerCode + " 开始生成的批次号为 " + batch.number);
        return batch;
    }

}
