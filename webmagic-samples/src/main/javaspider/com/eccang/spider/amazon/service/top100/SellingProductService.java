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
        for (SellingProduct product : products) {
            mDao.add(product);
        }
    }

    /**
     * 更新top100产品的库存数或爬取库存的状态信息
     */
    public void updateByObj(SellingProduct sellingProduct) {
        mDao.updateByObj(sellingProduct);
    }

    /**
     * 查询还未开始爬取库存的top100商品信息
     * @param num 查询数据条数
     */
    public List<SellingProduct> findNeedParseStockProduct(int num) {
        return mDao.findNeedParseStockProduct(num);
    }

    /**
     * 更新库存及库存抓取状态
     */
    public void updateStock(SellingProduct sellingProduct) {
        mDao.updateStock(sellingProduct);
    }

    /**
     * 更新top100产品表中，库存抓取状态
     */
    public void updateState(SellingProduct sellingProduct) {
        mDao.updateState(sellingProduct);
    }
}