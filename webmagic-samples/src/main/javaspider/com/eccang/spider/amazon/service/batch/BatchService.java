package com.eccang.spider.amazon.service.batch;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.batch.BatchDao;
import com.eccang.spider.amazon.pojo.batch.Batch;
import com.eccang.spider.amazon.pojo.batch.BatchAsin;
import com.eccang.spider.amazon.util.DateUtils;

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
    private BatchAsinService mBatchAsinService;

    /**
     * 添加Asin批次。
     * 1，生成批次总单；
     * 2，生成批次详单。
     *
     * @param type 0-Review全量爬取；1-监听爬取;2-Review更新爬取
     */
    public Batch addBatch(String customerCode, List<BatchAsin> batchAsinList, int type, int isImport) {

        Batch batch = generate(customerCode, type);
        batch.isImport = isImport;
        for (BatchAsin batchAsin : batchAsinList) {
            batchAsin.batchNumber = batch.number;
        }

        add(batch);

        mBatchAsinService.addAll(batchAsinList);

        return batch;
    }

    public void add(Batch batch) {
        mBatchDao.add(batch);
    }

    /**
     * @param type 0-全量爬取；1-监听爬取;2-Review更新爬取;3-跟卖;4-关键词排名
     */
    public Batch generate(String customerCode, int type) {

        Batch batch = new Batch();
        /* result like "EC20161110164710555" */
        batch.number = "EC" + DateUtils.format(new Date(), DateUtils.FORMAT_BATCH);
        batch.customerCode = customerCode;
        batch.type = type;
        batch.status = 0;

        mLogger.info("客户 " + customerCode + " 开始生成的批次号为 " + batch.number);
        return batch;
    }

    public Batch findByBatchNumber(String batchNumber) {
        return mBatchDao.findByBatchNumber(batchNumber);
    }

    public void update(Batch batch) {
        mBatchDao.update(batch);
    }

    public List<Batch> findByStatus(int... status) {
        List<Batch> list = new ArrayList<Batch>();
        for (int sta : status) {
            list.addAll(mBatchDao.findByStatus(sta));
        }
        for (Batch batch : list) {
            batch.status = 1;
            update(batch);
        }
        return list;
    }

}
