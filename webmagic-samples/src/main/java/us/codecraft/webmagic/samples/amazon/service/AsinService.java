package us.codecraft.webmagic.samples.amazon.service;

import com.beust.jcommander.ParameterException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.AsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;

import java.util.Date;
import java.util.List;

/**
 * Review业务
 */
@Service
public class AsinService {

    @Autowired
    AsinDao mAsinDao;

    private Logger mLogger = Logger.getLogger(getClass());

    /**
     * @param priority 级别，爬取什么星级以下的评论,取值范围[0-5]
     * @return
     */
    public List<Asin> find(int priority) {

        if (priority < 0 || priority > 5) {
            throw new ParameterException("priority must between 0 and 5");
        }

        int[] startStatus = {0, 0, 0, 0, 0};

        for (int i = 0; i < priority; i++) {
            startStatus[startStatus.length - i - 1] = 1;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = startStatus.length; i < len; i++) {
            sb.append(String.valueOf(startStatus[i]));
            if (i != len - 1) {
                sb.append("-");
            }
        }

        String status = sb.toString();//如果 priority = 3，拼凑出了 "0-0-1-1-1"
        mLogger.info("爬取的状态为：" + status);

        List<Asin> asinList = mAsinDao.find(status);

        if(CollectionUtils.isNotEmpty(asinList)) {
            for (Asin asin : asinList) {
                asin.saaSyncTime = new Date();
                mAsinDao.updateSyncTime(asin);
            }
        }

        return asinList;
    }
}
