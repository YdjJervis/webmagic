package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.SiteDao;
import us.codecraft.webmagic.samples.amazon.pojo.Site;

/**
 * Review业务
 */
@Service
public class SiteService {

    @Autowired
    SiteDao mSiteDao;

    /**
     * @param siteCode 站点码。eg:"CN"
     * @return 站点对象
     */
    public Site find(String siteCode) {
        return mSiteDao.find(siteCode).get(0);
    }
}
