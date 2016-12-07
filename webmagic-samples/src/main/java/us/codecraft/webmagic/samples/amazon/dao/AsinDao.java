package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Asin Dao层
 * @date 2016/10/11 18:00
 */
@Repository
public interface AsinDao extends BaseDao<Asin> {

    Asin findByAsin(String siteCode, String asin);

}
