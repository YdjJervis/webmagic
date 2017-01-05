package com.eccang.spider.amazon.service.dict;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.dict.SiteDao;
import com.eccang.spider.amazon.pojo.dict.Site;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 站点业务
 * @date 2016/10/11
 */
@Service
public class SiteService {

    @Autowired
    private SiteDao mSiteDao;

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

    public int update(Site site) {
        return mSiteDao.update(site);
    }

}
