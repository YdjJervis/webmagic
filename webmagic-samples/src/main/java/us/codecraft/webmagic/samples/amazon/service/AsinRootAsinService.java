package us.codecraft.webmagic.samples.amazon.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.AsinRootAsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.AsinRootAsin;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Asin业务
 * @date 2016/10/11
 */
@Service
public class AsinRootAsinService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private AsinRootAsinDao mDao;

    public AsinRootAsin find(String asin, String rootAsin) {
        return mDao.findByRelation(asin, rootAsin);
    }

    public AsinRootAsin findByAsin(String asin){
        return mDao.findByAsin(asin);
    }

    public void add(AsinRootAsin asinRootAsin) {
        if (find(asinRootAsin.asin, asinRootAsin.rootAsin) == null) {
            mDao.add(asinRootAsin);
        }
    }
}
