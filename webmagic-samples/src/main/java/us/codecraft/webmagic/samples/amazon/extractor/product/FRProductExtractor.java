package us.codecraft.webmagic.samples.amazon.extractor.product;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Product;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 法国站产品基本信息抽取器
 * @date 2016/12/24 18:14
 */
public class FRProductExtractor extends AbstractProductExtractor {

    @Override
    public Product extract(String asin, Page page) {
        super.extract(asin, page);
        sProduct.siteCode = R.SiteCode.UK;

        sProduct.reviewStar = page.getHtml().xpath("//*[@id='averageCustomerReviews']//span[@class='a-icon-alt']/text()").get();

        sProduct.addedTime = extractAddedTime(page);
        sProduct.category = extractRankInfo(page);

        return sProduct;
    }
}
