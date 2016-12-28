package us.codecraft.webmagic.samples.amazon.extractor.product;

import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Product;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

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

        List<Selectable> liNodes = page.getHtml().xpath("//div[@id='detail_bullets_id']//li[@id!='SalesRank']").nodes();
        for (Selectable liNode : liNodes) {
            String key = liNode.xpath("b/text()").get();
            if (StringUtils.isNotEmpty(key) && key.contains("Date")) {
                sProduct.modelType = 1;
                sProduct.addedTime = liNode.xpath("li/text()").get();
                break;
            }
        }
        sProduct.category = extractRankInfo(page);

        return sProduct;
    }

}
