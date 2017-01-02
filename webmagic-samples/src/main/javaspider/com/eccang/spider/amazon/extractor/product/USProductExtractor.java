package com.eccang.spider.amazon.extractor.product;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.crawl.Product;
import com.eccang.spider.amazon.pojo.ProductRank;
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

        Selectable model_1 = page.getHtml().xpath("//*[@id='productDetails_detailBullets_sections1']");
        if (StringUtils.isNotEmpty(model_1.get())) {
            sProduct.modelType = 1;
            for (Selectable trNode : page.getHtml().xpath("//*[@id='productDetails_detailBullets_sections1']/tbody/tr").nodes()) {
                String thText = trNode.xpath("th/text()").get();
                if (thText.contains("Date")) {
                    sProduct.addedTime = trNode.xpath("td/text()").get().trim();
                } else if (thText.contains("Rank")) {

                    List<ProductRank> rankList = new ArrayList<>();
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
            sProduct.category = extractRankInfo(page);

            return sProduct;
        }

        sProduct.modelType = 0;

        return sProduct;
    }

}
