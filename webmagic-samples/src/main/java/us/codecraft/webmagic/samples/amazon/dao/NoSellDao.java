package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 下架商品 Dao层
 * @date 2016/10/11 18:00
 */
@Repository
public interface NoSellDao extends BaseDao<Asin> {

    int findExist(String siteCode, String asin);

}
