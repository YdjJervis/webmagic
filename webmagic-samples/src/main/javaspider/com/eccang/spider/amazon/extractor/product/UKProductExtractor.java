package com.eccang.spider.amazon.extractor.product;

import us.codecraft.webmagic.Page;
import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.crawl.Product;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 美国站产品基本信息抽取器
 * @date 2016/12/24 18:14
 */
public class UKProductExtractor extends AbstractProductExtractor {

    @Override
    public Product extract(String asin, Page page) {
        super.extract(asin, page);
        sProduct.siteCode = R.SiteCode.UK;
        return sProduct;
    }

}
