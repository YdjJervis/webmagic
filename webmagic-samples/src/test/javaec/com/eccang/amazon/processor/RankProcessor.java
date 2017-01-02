package com.eccang.amazon.processor;

import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import com.eccang.spider.amazon.pojo.crawl.GoodsRankInfo;
import com.eccang.spider.base.util.UserAgentUtil;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 单页面保存测试，用于测试某些页面是否需要JS渲染
 * @date 2016/10/20 10:09
 */
public class RankProcessor implements PageProcessor {
    @Override
    public void process(Page page){
//        System.out.println(page.getHtml().get());
//        try {
//            FileUtils.writeStringToFile(new File("C:\\Users\\Administrator\\Desktop\\download.html"),page.getHtml().get());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        int adNum = 0;
        boolean isFind = false;
        List<Selectable> goodsNodesList = extractGoodsNodesList(page);
        if (goodsNodesList != null && goodsNodesList.size() > 0) {
            for (int i = 0; i < goodsNodesList.size(); i++) {
                Selectable goodsNode = goodsNodesList.get(i);
                /*判断是否是广告商品，是则统计其数量*/
                if (isAdvert(goodsNode)) {
                    adNum++;
                    continue;
                }

                /*判断是否是首页，是则解析前十的商品信息*/
                GoodsRankInfo goodsRankInfo = null;
                    goodsRankInfo = extractGoodsNode(goodsNode);

                /*将前十的商品信息入库*/
                //................
            }
        }
    }

    @Override
    public Site getSite() {
        return Site.me().setUserAgent(UserAgentUtil.getRandomUserAgent());
    }

    public static void main(String[] args) {
        Spider.create(new RankProcessor()).addUrl("https://www.amazon.com/s?url=search-alias%3Daps&keywords=watch&lo=none")
                .start();
    }

    /**
     * 解析商品排行信息列表
     */
    List<Selectable> extractGoodsNodesList(Page page) {
        return page.getHtml().xpath("//li[contains(@id, 'result_')]").nodes();
    }

    /**
     * 判断商品是不是广告
     */
    boolean isAdvert(Selectable goodsNode) {
        String ad = goodsNode.xpath("//h5[contains(@class, 's-sponsored-list-header')]").get();
        return StringUtils.isNotEmpty(ad);
    }

    /**
     * 判断是否是第一页
     */
    boolean isHomePage(String url) {
        return url.contains("page");
    }

    /**
     * 解析关键词搜索得到的商品信息
     */
    GoodsRankInfo extractGoodsNode(Selectable goodsNode) {
        /*获取商品asin*/
        GoodsRankInfo goodsRankInfo = new GoodsRankInfo();
        String title = goodsNode.xpath("//h2[contains(@class, 's-access-title')]/text()").get();
        String price = getPrice(goodsNode);
        String pictureUrl = goodsNode.xpath("//img[contains(@class, 'cfMarker')]/@src").get();
        int offersNum = getOffersNum(goodsNode);
        String departmentInfo = getDepartmentInfo(goodsNode);
        String deliveryMode = getDeliveryMode(goodsNode);
        String goodsStatus = getGoodsStatus(goodsNode);
        String distributionMode = getDistributionMode(goodsNode) ? "free" : "notFree";
        goodsRankInfo.setTitle(title);
        goodsRankInfo.setPrice(price);
        goodsRankInfo.setGoodsPictureUrl(pictureUrl);
        goodsRankInfo.setOffersNum(offersNum);
        goodsRankInfo.setDepartmentInfo(departmentInfo);
        goodsRankInfo.setDeliveryMode(deliveryMode);
        goodsRankInfo.setGoodsStatus(goodsStatus);
        goodsRankInfo.setDistributionMode(distributionMode);

        return goodsRankInfo;
    }

    /**
     * 解析商品ASIN
     */
    String getGoodsAsin(Selectable goodsNode) {
        return goodsNode.xpath("/li/@data-asin").get();
    }

    /**
     * 解析商品价格
     */
    String getPrice(Selectable goodsNode) {
        String price = "";
        List<Selectable> priceNodes = goodsNode.xpath("//span[contains(@class, 'sx-price')]/*").nodes();
        for (Selectable priceNode : priceNodes) {
            String classStr = priceNode.xpath("/*/@class").get();
            if (classStr.contains("fractional")) {
                price += ("." + priceNode.xpath("/*/text()").get());
            } else {
                price += priceNode.xpath("/*/text()").get();
            }
        }
        return price;
    }

    /**
     * 解析商品跟卖数
     */
    int getOffersNum(Selectable goodsNode) {
        int offersNum = 0;
        List<String> resultList = goodsNode.regex("(\\d+) offer").all();
        for (String offer : resultList) {
            offersNum += Integer.valueOf(offer);
        }
        return offersNum;
    }

    /**
     * 解析排名商品的品类信息
     */
    String getDepartmentInfo(Selectable goodsNode) {
        String departmentInfo = null;
        departmentInfo = goodsNode.xpath("//a/span[@class='a-text-bold']/text()").get();

        departmentInfo += goodsNode.xpath("//a[@class='a-size-small a-link-normal a-text-normal' and contains(@href,'/s/')]/text()").get();
        //departmentInfo += goodsNode.regex("(See all.*?items)").get();
        return departmentInfo;
    }

    /**
     * 解析排名商品的发货方式
     */
    String getDeliveryMode(Selectable goodsNode) {
        String deliveryMode = goodsNode.xpath("//i[contains(@class, 'a-icon-prime')]/span/text()").get();
        if (StringUtils.isNotEmpty(deliveryMode)) {
            return deliveryMode.trim().equalsIgnoreCase("Prime") ? "Prime" : "Others";
        }
        return "Others";
    }

    /**
     * 解析排名商品的状态（Bestseller or Gesponsert）
     */
    String getGoodsStatus(Selectable goodsNode) {
        StringBuffer goodsStatus = new StringBuffer();
        String bestSeller = goodsNode.xpath("//span[contains(@class, 'sx-bestseller-color')]/span/text()").get();
        String sponsored = goodsNode.xpath("//h5[contains(@class, 's-sponsored-list-header')]/text()").get();
        if (StringUtils.isNotEmpty(bestSeller) && StringUtils.isNotEmpty(sponsored)) {
            goodsStatus.append(bestSeller.trim());
            goodsStatus.append(" and ");
            goodsStatus.append(sponsored.trim());
        } else if (StringUtils.isEmpty(bestSeller) && StringUtils.isEmpty(sponsored)) {
            return null;
        } else {
            goodsStatus.append(bestSeller);
            goodsStatus.append(sponsored);
        }

        return goodsStatus.toString().trim();
    }

    /**
     * 解析排名商品是否免运费
     */
    boolean getDistributionMode(Selectable goodsNode) {
        return goodsNode.regex("FREE Shipping").match();
    }
}