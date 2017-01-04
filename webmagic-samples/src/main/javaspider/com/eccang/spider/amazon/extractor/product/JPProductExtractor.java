package com.eccang.spider.amazon.extractor.product;

import us.codecraft.webmagic.Page;
import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.ProductRank;
import com.eccang.spider.amazon.pojo.crawl.Product;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 美国站产品基本信息抽取器
 * @date 2016/12/24 18:14
 */
public class JPProductExtractor extends AbstractProductExtractor {

    @Override
    public Product extract(String asin, Page page) {
        super.extract(asin, page);
        sProduct.siteCode = R.SiteCode.JP;
        return sProduct;
    }

    @Override
    String extractAddedTime(Page page) {
        return page.getHtml().xpath("//*[@class='date-first-available']/td[2]/text()").get();
    }

    @Override
    ProductRank extractTotalRank(Page page) {

        ProductRank rank = new ProductRank();
        rank.rank = page.getHtml().xpath("//tr[@id='SalesRank']/td[@class='value']/text()").get();
        ProductRank.Category category = rank.new Category();
        category.category = page.getHtml().xpath("//tr[@id='SalesRank']/td[@class='value']/a/text()").get();
        category.url = page.getHtml().xpath("//tr[@id='SalesRank']/td[@class='value']/a/@href").get();

        rank.categoryList.add(category);
        return rank;
    }
}
