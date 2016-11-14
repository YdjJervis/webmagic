package us.codecraft.webmagic.samples.amazon.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.BatchAsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.BatchAsin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户 业务
 * @date 2016/10/11
 */
@Service
public class BatchAsinService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private BatchAsinDao mBatchAsinDao;

    public void add(BatchAsin batchAsin) {

        if (!isExist(batchAsin)) {
            mBatchAsinDao.add(batchAsin);
        } else {
            update(batchAsin);
        }
    }

    public void addAll(List<BatchAsin> batchAsinList) {

        List<BatchAsin> newList = new ArrayList<BatchAsin>();

        for (BatchAsin batchAsin : batchAsinList) {
            if (!isExist(batchAsin)) {
                newList.add(batchAsin);
            } else {
                update(batchAsin);
            }
        }

        if (CollectionUtils.isNotEmpty(newList)) {
            mBatchAsinDao.addAll(newList);
        }
    }

    public void update(BatchAsin batchAsin) {
        mBatchAsinDao.update(batchAsin);
    }

    public boolean isExist(BatchAsin batchAsin) {
        return mBatchAsinDao.find(batchAsin) != null;
    }

    public List<BatchAsin> findAllByAsin(String asin) {
        return mBatchAsinDao.findAllByAsin(asin);
    }

    public List<BatchAsin> findAllByBatchNum(String batchNumber) {
        return mBatchAsinDao.findAllByBatchNum(batchNumber);
    }

    public void updateAll(List<BatchAsin> batchAsinList) {
        for (BatchAsin batchAsin : batchAsinList) {
            update(batchAsin);
        }
    }


}
