package com.eccang.spider.amazon.service.crawl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eccang.spider.amazon.dao.crawl.ProductDao;
import com.eccang.spider.amazon.pojo.crawl.Product;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户 业务
 * @date 2016/10/11
 */
@Service
public class ProductService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private ProductDao mDao;

    public void add(Product product) {
        if (!isExist(product)) {
            mDao.add(product);
        } else {
            update(product);
        }
    }

    public void update(Product product) {
        mDao.update(product);
    }

    public boolean isExist(Product product) {
        return mDao.findByObject(product) != null;
    }
}
