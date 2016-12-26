package us.codecraft.webmagic.samples.amazon.dao.dict;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.dict.Site;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 网站字典表Dao
 * @date 2016/10/11 18:00
 */
@Repository
public interface SiteDao extends BaseDao<Site> {

    /**
     * @param domain 协议+域名，eg：https://www.amazon.cn
     * @return Site对象
     */
    Site findByDomain(String domain);
}
