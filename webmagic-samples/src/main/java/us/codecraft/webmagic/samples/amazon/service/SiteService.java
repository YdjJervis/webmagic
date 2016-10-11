package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.SiteDao;
import us.codecraft.webmagic.samples.amazon.pojo.Site;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 站点业务
 * @date 2016/10/11
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

    /**
     * 根据域名找Site对象
     */
    public Site findByDomain(String domain) {
        return mSiteDao.findByDomain(domain);
    }

}
