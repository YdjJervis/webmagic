package us.codecraft.webmagic.samples.amazon.extractor.product;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Product;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 产品抽取器接口
 * @date 2016/12/24 18:11
 */
public interface ProductExtractor {

    Product extract(String asin, Page page);
}
