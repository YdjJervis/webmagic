package us.codecraft.webmagic.samples.amazon.extractor.product;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.ProductRank;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Product;

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
    String getReviewStar(Page page) {
        return page.getHtml().xpath("//*[@id='averageCustomerReviews']//span[@class='a-icon-alt']/text()").get();
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
