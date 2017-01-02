package com.eccang.spider.amazon.dao.crawl;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.crawl.Product;
import com.eccang.spider.base.dao.BaseDao;

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
