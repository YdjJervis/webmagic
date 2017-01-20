package com.eccang.spider.amazon.service.top100;

import com.eccang.spider.amazon.dao.top100.SellingProductDao;
import com.eccang.spider.amazon.pojo.top100.SellingProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/19 15:47
 */
@Service
public class SellingProductService {
    @Autowired
    SellingProductDao mDao;

    public void addAll(List<SellingProduct> products) {
        mDao.addAll(products);
    }
}