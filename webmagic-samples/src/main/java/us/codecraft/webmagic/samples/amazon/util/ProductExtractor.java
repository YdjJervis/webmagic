package us.codecraft.webmagic.samples.amazon.util;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.Product;
import us.codecraft.webmagic.samples.amazon.pojo.ProductRank;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 产品抽取
 * @date 2016/12/1 13:56
 */
public class ProductExtractor {

    private String mSiteCode;
    private String mAsin;

    private Page mPage;

    public ProductExtractor(String siteCode, String asin, Page page) {
        mSiteCode = siteCode;
        mPage = page;
        mAsin = asin;
    }

    public Product extract() {

        if (mSiteCode.equals("US")) {
            return extractUS();
        }

        return null;
    }

    private Product extractUS() {//抽取美国产品

        Product product = new Product();
        product.siteCode = mSiteCode;
        product.rootAsin = mAsin;
        List<Selectable> merchantNodes = mPage.getHtml().xpath("//*[@id='merchant-info']/a").nodes();
        if (merchantNodes.size() == 2) {
            product.sellerID = merchantNodes.get(0).xpath("a/@href").regex("seller=([0-9a-zA-Z]*)").get();
            product.sellerName = merchantNodes.get(0).xpath("a/text()").get();

            product.transID = merchantNodes.get(1).xpath("a/@href").regex("nodeId=([0-9]*)").get();
            product.transName = merchantNodes.get(1).xpath("a/text()").get();
        } else if (merchantNodes.size() == 1) {
            product.sellerID = merchantNodes.get(0).xpath("a/@href").regex("seller=([0-9a-zA-Z]*)").get();
            product.sellerName = merchantNodes.get(0).xpath("a/text()").get();
        }

        product.sellerID = mPage.getHtml().xpath("//*[@id='merchant-info']/html()").regex("seller=([0-9a-zA-Z]*)").get();
        product.title = mPage.getHtml().xpath("//*[@id='productTitle']/text()").get();
        product.price = mPage.getHtml().xpath("//*[@id='priceblock_ourprice' or @id='priceblock_dealprice' or @id='priceblock_saleprice']/text()").get();
        product.imgUrl = mPage.getHtml().xpath("//*[@id='imgTagWrapperId']/img/@data-a-dynamic-image").regex("(http.*?jpg)").get();
        product.reviewNum = mPage.getHtml().xpath("*[@id='acrCustomerReviewText']/text()").regex("([0-9,]*)").get();
        product.reviewStar = mPage.getHtml().xpath("*[@id='reviewStarsLinkedCustomerReviews']//span/text()").get();
        product.reviewTime = mPage.getHtml().xpath("//div[@id='revMHRL']/div/div/span/span[2]/text()").get();
        product.replyNum = mPage.getHtml().xpath("a[@id='askATFLink']/html()").regex("([0-9,]+)").get();
        product.transMode = mPage.getHtml().xpath("*[@id='priceBadging_feature_div']//span[@class='a-icon-alt']/text()").get();
        product.sellerNum = mPage.getHtml().xpath("//*[@id='mbc']//a[contains(@href,'offer-listing')]/text()").regex("\\(([0-9]*)\\)").get();

        List<String> featureList = mPage.getHtml().xpath("//div[@id='feature-bullets']//span[@class='a-list-item']/text()").all();
        System.out.println(featureList);
        product.extra = new Gson().toJson(featureList);

        Selectable model_1 = mPage.getHtml().xpath("//*[@id='productDetails_detailBullets_sections1']");
        if (StringUtils.isNotEmpty(model_1.get())) {
            product.modelType = 1;
            for (Selectable trNode : mPage.getHtml().xpath("//*[@id='productDetails_detailBullets_sections1']/tbody/tr").nodes()) {
                String thText = trNode.xpath("th/text()").get();
                if (thText.contains("Date")) {
                    product.addedTime = trNode.xpath("td/text()").get().trim();
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

                    product.category = new Gson().toJson(rankList);
                }
            }
            return product;
        }

        Selectable model_2 = mPage.getHtml().xpath("//*[@id='detailBulletsWrapper_feature_div'] | //div[@id='detail-bullets']//div[@class='content']");
        if (StringUtils.isNotEmpty(model_2.get())) {
            product.modelType = 2;
            List<Selectable> spanNodes = model_2.xpath("//*[@id='detailBullets_feature_div']//span[@class='a-list-item']").nodes();
            for (Selectable spanNode : spanNodes) {
                if (spanNode.xpath("span/span/text()").get().contains("Date")) {
                    product.addedTime = spanNode.xpath("span/span[2]/text()").get();
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
            product.category = new Gson().toJson(rankList);

            return product;
        }

        product.modelType = 0;

        return product;

    }


}