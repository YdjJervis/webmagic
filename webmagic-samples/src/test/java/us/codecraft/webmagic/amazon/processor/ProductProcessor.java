package us.codecraft.webmagic.amazon.processor;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.samples.amazon.pojo.Product;
import us.codecraft.webmagic.samples.amazon.pojo.ProductRank;
import us.codecraft.webmagic.samples.base.util.UserAgentUtil;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 单页面保存测试，用于测试某些页面是否需要JS渲染
 * @date 2016/10/20 10:09
 */
public class ProductProcessor implements PageProcessor {
    @Override
    public void process(Page page) {
        /*System.out.println(page.getHtml().get());
        try {
            FileUtils.writeStringToFile(new File("C:\\Users\\Administrator\\Desktop\\download.html"),page.getHtml().get());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println(extractProduct(page));
    }

    private Product extractProduct(Page page) {
        Product product = new Product();
        List<Selectable> merchantNodes = page.getHtml().xpath("//*[@id='merchant-info']/a").nodes();
        if (merchantNodes.size() == 2) {
            product.sellerID = merchantNodes.get(0).xpath("a/@href").regex("seller=([0-9a-zA-Z]*)").get();
            product.sellerName = merchantNodes.get(0).xpath("a/text()").get();

            product.transID = merchantNodes.get(1).xpath("a/@href").regex("nodeId=([0-9]*)").get();
            product.transName = merchantNodes.get(1).xpath("a/text()").get();
        } else if (merchantNodes.size() == 1) {
            product.sellerID = merchantNodes.get(0).xpath("a/@href").regex("seller=([0-9a-zA-Z]*)").get();
            product.sellerName = merchantNodes.get(0).xpath("a/text()").get();
        }

        product.sellerID = page.getHtml().xpath("//*[@id='merchant-info']/html()").regex("seller=([0-9a-zA-Z]*)").get();
        product.title = page.getHtml().xpath("//*[@id='productTitle']/text()").get();
        product.price = page.getHtml().xpath("//*[@id='priceblock_ourprice' or @id='priceblock_dealprice' or @id='priceblock_saleprice']/text()").get();
        product.imgUrl = page.getHtml().xpath("//*[@id='imgTagWrapperId']/img/@data-a-dynamic-image").regex("(http.*?jpg)").get();
        product.reviewNum = page.getHtml().xpath("*[@id='acrCustomerReviewText']/text()").regex("([0-9,]*)").get();
        product.reviewStar = page.getHtml().xpath("*[@id='reviewStarsLinkedCustomerReviews']//span/text()").get();
        product.reviewTime = page.getHtml().xpath("//div[@id='revMHRL']/div/div/span/span[2]/text()").get();
        product.replyNum = page.getHtml().xpath("a[@id='askATFLink']/html()").regex("([0-9,]+)").get();
        product.transMode = page.getHtml().xpath("*[@id='priceBadging_feature_div']//span[@class='a-icon-alt']/text()").get();
        product.sellerNum = page.getHtml().xpath("//*[@id='mbc']//a[contains(@href,'offer-listing')]/text()").regex("\\(([0-9]*)\\)").get();

        List<String> featureList = page.getHtml().xpath("//div[@id='feature-bullets']//span[@class='a-list-item']/text()").all();
        System.out.println(featureList);
        product.extra = new Gson().toJson(featureList);

        Selectable model_1 = page.getHtml().xpath("//*[@id='productDetails_detailBullets_sections1']");
        if (StringUtils.isNotEmpty(model_1.get())) {
            product.modelType = 1;
            for (Selectable trNode : page.getHtml().xpath("//*[@id='productDetails_detailBullets_sections1']/tbody/tr").nodes()) {
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

        Selectable model_2 = page.getHtml().xpath("//*[@id='detailBulletsWrapper_feature_div'] | //div[@id='detail-bullets']//div[@class='content']");

        /*if (StringUtils.isEmpty(model_2.get())) {
            model_2 = page.getHtml().xpath("");
        }*/
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

    @Override
    public Site getSite() {
        return Site.me().setUserAgent(UserAgentUtil.getRandomUserAgent());
    }

    /**
     * 无需网络请求，测试精确提取规则
     */
    @Test
    public void testExtractPageUnit() {

        Page page = new Page();

        String htmlUnit = "<tbody>\n" +
                " <tr> \n" +
                "  <th class=\"a-color-secondary a-size-base prodDetSectionEntry\"> Product Dimensions </th> \n" +
                "  <td class=\"a-size-base\"> 6 x 3.5 x 2.1 inches </td> \n" +
                " </tr> \n" +
                " <tr> \n" +
                "  <th class=\"a-color-secondary a-size-base prodDetSectionEntry\"> Best Sellers Rank </th> \n" +
                "  <td> <span> <span>#31,628 in Cell Phones &amp; Accessories (<a href=\"https://www.amazon.com/gp/bestsellers/wireless/ref=pd_dp_ts_cps_1\">See Top 100 in Cell Phones &amp; Accessories</a>)</span> <br> <span>#836 in <a href=\"https://www.amazon.com/gp/bestsellers/wireless//ref=pd_zg_hrsr_cps_1_1\">Cell Phones &amp; Accessories</a> &gt; <a href=\"https://www.amazon.com/gp/bestsellers/wireless/2407749011/ref=pd_zg_hrsr_cps_1_2_last\">Unlocked Cell Phones</a></span> <br> </span> </td> \n" +
                " </tr> \n" +
                "</tbody>";
        page.setHtml(new Html(htmlUnit));
        System.out.println(page.getHtml());
        Selectable xpath = page.getHtml().xpath("//a[@id='a']");
        System.out.println(StringUtils.isEmpty(xpath.get()));

    }

    public static void main(String[] args) {
        //model_1 https://www.amazon.com/dp/B01LW0F62Q
        //model_2 https://www.amazon.com/dp/B01JBYXUQ6
        //model_3 https://www.amazon.com/dp/B00RM7AAA4
        Spider.create(new ProductProcessor()).addUrl("https://www.amazon.com/dp/B00RM7AAA4")
                .start();
    }
}