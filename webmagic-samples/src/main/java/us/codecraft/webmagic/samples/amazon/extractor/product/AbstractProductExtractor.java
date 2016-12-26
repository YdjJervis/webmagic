package us.codecraft.webmagic.samples.amazon.extractor.product;

import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.Product;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 产品抽取器抽象类，做一些公共操作
 * @date 2016/12/24 18:12
 */
public abstract class AbstractProductExtractor implements ProductExtractor{

    Logger sLogger = Logger.getLogger(getClass());
    Product sProduct;

    @Override
    public Product extract(String asin, Page page) {
        sProduct = new Product();
        return sProduct;
    }
}
