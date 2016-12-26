package us.codecraft.webmagic.samples.amazon.service.relation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.AsinRootAsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.relation.AsinRootAsin;

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

    public AsinRootAsin find(String asin, String rootAsin, String siteCode) {
        return mDao.findByRelation(asin, rootAsin, siteCode);
    }

    public AsinRootAsin findByAsin(String asin, String siteCode){
        return mDao.findByAsin(asin, siteCode);
    }

    public void add(AsinRootAsin asinRootAsin) {
        if (find(asinRootAsin.asin, asinRootAsin.rootAsin, asinRootAsin.siteCode) == null) {
            mDao.add(asinRootAsin);
        }
    }
}
