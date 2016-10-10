package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Site;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * 网站字典表Dao
 */
@Repository
public interface SiteDao extends BaseDao<Site> {

    /**
     * @param domain 协议+域名，eg：https://www.amazon.cn
     * @return Site对象
     */
    Site findByDomain(String domain);
}
