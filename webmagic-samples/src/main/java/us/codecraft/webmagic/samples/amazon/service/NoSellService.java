package us.codecraft.webmagic.samples.amazon.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.NoSellDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 下架商品 业务
 * @date 2016/12/7
 */
@Service
public class NoSellService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private NoSellDao mDao;

    public void add(Asin asin) {
        if (!isExist(asin)) {
            mDao.add(asin);
        }
    }

    public boolean isExist(Asin asin) {
        return mDao.findExist(asin.siteCode, asin.rootAsin) > 0;
    }

}
