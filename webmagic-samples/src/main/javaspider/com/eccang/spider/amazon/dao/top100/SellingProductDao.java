package com.eccang.spider.amazon.dao.top100;

import com.eccang.spider.amazon.pojo.top100.SellingProduct;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/19 14:45
 */
@Repository
public interface SellingProductDao extends BaseDao<SellingProduct> {

    int count(String depUrl, String batchNum, String asin);

    void updateByObj(SellingProduct sellingProduct);

    List<SellingProduct> findNeedParseStockProduct(int num);

    void updateStock(SellingProduct sellingProduct);

    void updateState(SellingProduct sellingProduct);
}
