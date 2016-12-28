package us.codecraft.webmagic.samples.amazon.extractor.product;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Product;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 产品抽取站点适配器
 * @date 2016/12/26 9:15
 */
public class ProductExtractorAdapter {

    public Product extract(String siteCode, String asin, Page page) {
        ProductExtractor extractor = null;
        if (siteCode.equals(R.SiteCode.US)) {
            extractor = new USProductExtractor();
        }else if(siteCode.equals(R.SiteCode.UK)){
            extractor = new UKProductExtractor();
        }

        if (extractor != null) {
            return extractor.extract(asin, page);
        } else {
            return null;
        }
    }
}
