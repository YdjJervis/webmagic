package us.codecraft.webmagic.samples.amazon.extractor.product;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.Product;
import us.codecraft.webmagic.samples.amazon.pojo.ProductRank;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 产品抽取器抽象类，做一些公共操作
 * @date 2016/12/24 18:12
 */
public abstract class AbstractProductExtractor implements ProductExtractor{

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
        sProduct.reviewTime = page.getHtml().xpath("//div[@id='revMHRL']/div/div/span/span[2]/text()").get();
        sProduct.sellerNum = page.getHtml().xpath("//*[@id='mbc']//a[contains(@href,'offer-listing')]/text()").get();
        sProduct.addedTime = extractAddedTime(page);

        sProduct.reviewStar = getReviewStar(page);
        sProduct.category = extractRankInfo(page);

        return sProduct;
    }

    /**
     * @return
     */
    String getReviewStar(Page page) {
        return page.getHtml().xpath("//*[@id='reviewStarsLinkedCustomerReviews']//span/text()").get();
    }

    /**
     * @return 排名信息，Json串
     */
    String extractRankInfo(Page page) {
        List<ProductRank> rankList = new ArrayList<>();

        /* 解析总排名情况 */
        ProductRank rank = new ProductRank();
        rank.rank = page.getHtml().xpath("//*[@id='SalesRank']/text()").get();
        page.getHtml().xpath("//*[@id='SalesRank']/text()").regex(".*([0-9,]*).*");
        ProductRank.Category category = rank.new Category();
        category.category = page.getHtml().xpath("//*[@id='SalesRank']/a/text()").get();
        category.url = page.getHtml().xpath("//*[@id='SalesRank']/a/@href").get();
        rank.categoryList.add(category);

        rankList.add(rank);

        /* 解析在其它分类的排名情况 */
        for (Selectable liNode : page.getHtml().xpath("//*[@class='zg_hrsr_item']").nodes()) {
            rank = new ProductRank();
            rank.rank = liNode.xpath("span/text()").get();

            for (Selectable aNode : liNode.xpath("//a").nodes()) {
                category = rank.new Category();
                category.category = aNode.xpath("a/text()").get();
                category.url = aNode.xpath("a/@href").get();
                rank.categoryList.add(category);
            }
            rankList.add(rank);
        }
        return new Gson().toJson(rankList);
    }

    /**
     * @return 产品添加时间
     */
    private String extractAddedTime(Page page) {
        List<Selectable> liNodes = page.getHtml().xpath("//div[@id='detail_bullets_id']//li[@id!='SalesRank']").nodes();
        for (Selectable liNode : liNodes) {
            String key = liNode.xpath("b/text()").get();
            if (StringUtils.isNotEmpty(key) && key.contains(onAddedTimeKey())) {
                return liNode.xpath("li/text()").get();
            }
        }
        return null;
    }

    /**
     * @return 详细信息对应的Key值包含的关键字
     */
    String onAddedTimeKey(){
        return "Date";
    }
}
