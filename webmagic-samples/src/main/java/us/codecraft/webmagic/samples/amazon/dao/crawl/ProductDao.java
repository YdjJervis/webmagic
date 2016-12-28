package us.codecraft.webmagic.samples.amazon.dao.crawl;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Product;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论Dao
 * @date 2016/10/11 18:00
 */
@Repository
public interface ProductDao extends BaseDao<Product> {

    Product findByObject(Product product);
}
