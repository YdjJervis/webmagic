package us.codecraft.webmagic.samples.amazon.extractor.product;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Product;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 德国站产品基本信息抽取器
 * @date 2016/12/24 18:14
 */
public class DEProductExtractor extends AbstractProductExtractor {

    @Override
    public Product extract(String asin, Page page) {
        super.extract(asin, page);
        sProduct.siteCode = R.SiteCode.DE;
        return sProduct;
    }

    @Override
    String onAddedTimeKey() {
        return "seit";
    }
}
