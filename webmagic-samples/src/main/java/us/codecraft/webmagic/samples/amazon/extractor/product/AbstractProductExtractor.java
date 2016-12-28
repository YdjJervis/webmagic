package us.codecraft.webmagic.samples.amazon.extractor.product;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.Product;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

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
        sProduct.rootAsin = asin;

        List<Selectable> merchantNodes = page.getHtml().xpath("//*[@id='merchant-info']/a").nodes();
        if (merchantNodes.size() == 2) {
            sProduct.sellerID = merchantNodes.get(0).xpath("a/@href").regex("seller=([0-9a-zA-Z]*)").get();
            sProduct.sellerName = merchantNodes.get(0).xpath("a/text()").get();

            sProduct.transID = merchantNodes.get(1).xpath("a/@href").regex("nodeId=([0-9]*)").get();
            sProduct.transName = merchantNodes.get(1).xpath("a/text()").get();
        } else if (merchantNodes.size() == 1) {
            sProduct.sellerID = merchantNodes.get(0).xpath("a/@href").regex("seller=([0-9a-zA-Z]*)").get();
            sProduct.sellerName = merchantNodes.get(0).xpath("a/text()").get();
        }

        List<String> featureList = page.getHtml().xpath("//div[@id='feature-bullets']//span[@class='a-list-item']/text()").all();
        sProduct.extra = new Gson().toJson(featureList);

        sProduct.title = page.getHtml().xpath("//*[@id='productTitle']/text()").get();
        sProduct.price = page.getHtml().xpath("//*[@id='priceblock_ourprice' or @id='priceblock_dealprice' or @id='priceblock_saleprice']/text()").get();
        sProduct.imgUrl = page.getHtml().xpath("//*[@id='imgTagWrapperId']/img/@data-a-dynamic-image").regex("(http.*?jpg)").get();
        sProduct.reviewNum = page.getHtml().xpath("//*[@id='acrCustomerReviewText']/text()").regex("([0-9,]*)").get();
        sProduct.replyNum = page.getHtml().xpath("//a[@id='askATFLink']/html()").regex("([0-9,]+)").get();
        sProduct.reviewStar = page.getHtml().xpath("//*[@id='reviewStarsLinkedCustomerReviews']//span/text()").get();
        sProduct.reviewTime = page.getHtml().xpath("//div[@id='revMHRL']/div/div/span/span[2]/text()").get();
        sProduct.sellerNum = page.getHtml().xpath("//*[@id='mbc']//a[contains(@href,'offer-listing')]/text()").get();

        return sProduct;
    }
}
