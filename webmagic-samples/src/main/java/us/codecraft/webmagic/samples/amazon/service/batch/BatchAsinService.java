package us.codecraft.webmagic.samples.amazon.service.batch;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.batch.BatchAsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.batch.BatchAsin;

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

    public BatchAsin findAllByAsin(String batchNum, String siteCode, String asin) {
        return mBatchAsinDao.findAllByAsin(batchNum, siteCode, asin);
    }

    public List<BatchAsin> findAllByBatchNum(String batchNumber) {
        return mBatchAsinDao.findAllByBatchNum(batchNumber);
    }

    public void updateAll(List<BatchAsin> batchAsinList) {
        for (BatchAsin batchAsin : batchAsinList) {
            update(batchAsin);
        }
    }

    public List<BatchAsin> findByStatus(int status) {
        return mBatchAsinDao.findByStatus(status);
    }

    /**
     * @return 没有爬取首页的详单列表
     */
    public List<BatchAsin> findNotCrawledMainPage() {
        return mBatchAsinDao.findNotCrawledMainPage();
    }

    public List<BatchAsin> findNotCrawledReview() {
        return mBatchAsinDao.findNotCrawledReview();
    }

    public List<BatchAsin> findNotUpdatedReview() {
        return mBatchAsinDao.findNotUpdatedReview();
    }

    public float findAverageProgress(String batchNumber) {
        return mBatchAsinDao.findAverageProgress(batchNumber);
    }

    /**
     * eg:把"0-0-1-1-1"转换成[1,2,3]
     */
    public List<Integer> getStarArray(String starExp) {
        String[] split = starExp.split("-");
        List<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("1")) {
                list.add(5 - i);
            }
        }

        return list;
    }


}
