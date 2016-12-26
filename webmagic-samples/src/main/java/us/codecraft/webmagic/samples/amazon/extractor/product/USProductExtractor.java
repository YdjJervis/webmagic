package us.codecraft.webmagic.samples.amazon.extractor.product;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.Product;
import us.codecraft.webmagic.samples.amazon.pojo.ProductRank;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 美国站产品基本信息抽取器
 * @date 2016/12/24 18:14
 */
public class USProductExtractor extends AbstractProductExtractor {

    @Override
    public Product extract(String asin, Page page) {
        super.extract(asin, page);
        sProduct.siteCode = R.SiteCode.US;
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

        sProduct.sellerID = page.getHtml().xpath("//*[@id='merchant-info']/html()").regex("seller=([0-9a-zA-Z]*)").get();
        sProduct.title = page.getHtml().xpath("//*[@id='productTitle']/text()").get();
        sProduct.price = page.getHtml().xpath("//*[@id='priceblock_ourprice' or @id='priceblock_dealprice' or @id='priceblock_saleprice']/text()").get();
        sProduct.imgUrl = page.getHtml().xpath("//*[@id='imgTagWrapperId']/img/@data-a-dynamic-image").regex("(http.*?jpg)").get();
        sProduct.reviewNum = page.getHtml().xpath("*[@id='acrCustomerReviewText']/text()").regex("([0-9,]*)").get();
        sProduct.reviewStar = page.getHtml().xpath("*[@id='reviewStarsLinkedCustomerReviews']//span/text()").get();
        sProduct.reviewTime = page.getHtml().xpath("//div[@id='revMHRL']/div/div/span/span[2]/text()").get();
        sProduct.replyNum = page.getHtml().xpath("a[@id='askATFLink']/html()").regex("([0-9,]+)").get();
        sProduct.transMode = page.getHtml().xpath("*[@id='priceBadging_feature_div']//span[@class='a-icon-alt']/text()").get();
        sProduct.sellerNum = page.getHtml().xpath("//*[@id='mbc']//a[contains(@href,'offer-listing')]/text()").regex("\\(([0-9]*)\\)").get();

        List<String> featureList = page.getHtml().xpath("//div[@id='feature-bullets']//span[@class='a-list-item']/text()").all();
        sLogger.info(featureList);
        sProduct.extra = new Gson().toJson(featureList);

        Selectable model_1 = page.getHtml().xpath("//*[@id='productDetails_detailBullets_sections1']");
        if (StringUtils.isNotEmpty(model_1.get())) {
            sProduct.modelType = 1;
            for (Selectable trNode : page.getHtml().xpath("//*[@id='productDetails_detailBullets_sections1']/tbody/tr").nodes()) {
                String thText = trNode.xpath("th/text()").get();
                if (thText.contains("Date")) {
                    sProduct.addedTime = trNode.xpath("td/text()").get().trim();
                } else if (thText.contains("Rank")) {

                    List<ProductRank> rankList = new ArrayList<ProductRank>();
                    for (Selectable spanNode : trNode.xpath("td/span/span").nodes()) {
                        ProductRank rank = new ProductRank();
                        rank.rank = spanNode.regex("#([0-9,]*)").get();
                        for (Selectable aNode : spanNode.xpath("a").nodes()) {
                            ProductRank.Category category = rank.new Category();
                            category.category = aNode.xpath("a/text()").get();
                            category.url = aNode.xpath("a/@href").get();
                            rank.categoryList.add(category);
                        }
                        rankList.add(rank);
                    }

                    sProduct.category = new Gson().toJson(rankList);
                }
            }
            return sProduct;
        }

        Selectable model_2 = page.getHtml().xpath("//*[@id='detailBulletsWrapper_feature_div'] | //div[@id='detail-bullets']//div[@class='content']");
        if (StringUtils.isNotEmpty(model_2.get())) {
            sProduct.modelType = 2;
            List<Selectable> spanNodes = model_2.xpath("//*[@id='detailBullets_feature_div']//span[@class='a-list-item']").nodes();
            for (Selectable spanNode : spanNodes) {
                if (spanNode.xpath("span/span/text()").get().contains("Date")) {
                    sProduct.addedTime = spanNode.xpath("span/span[2]/text()").get();
                }
            }

            List<ProductRank> rankList = new ArrayList<ProductRank>();

            /* 解析总排名情况 */
            ProductRank rank = new ProductRank();
            rank.rank = model_2.xpath("//*[@id='SalesRank']/text()").regex("#([0-9,]*)").get();
            ProductRank.Category category = rank.new Category();
            category.category = model_2.xpath("//*[@id='SalesRank']/a/text()").get();
            category.url = model_2.xpath("//*[@id='SalesRank']/a/@href").get();
            rank.categoryList.add(category);

            rankList.add(rank);

            /* 解析在其它分类的排名情况 */
            for (Selectable liNode : model_2.xpath("//*[@class='zg_hrsr_item']").nodes()) {
                rank = new ProductRank();
                rank.rank = liNode.xpath("span/text()").regex("#([0-9,]*)").get();

                for (Selectable aNode : liNode.xpath("//a").nodes()) {
                    category = rank.new Category();
                    category.category = aNode.xpath("a/text()").get();
                    category.url = aNode.xpath("a/@href").get();
                    rank.categoryList.add(category);
                }
                rankList.add(rank);
            }
            sProduct.category = new Gson().toJson(rankList);

            return sProduct;
        }

        sProduct.modelType = 0;

        return sProduct;
    }

}
