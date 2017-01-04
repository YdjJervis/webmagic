package com.eccang.spider.amazon.extractor.followsell;

import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Page;
import com.eccang.spider.amazon.R;
import com.eccang.spider.amazon.pojo.crawl.FollowSell;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 跟卖抽取器抽象类，做一些公共操作
 * @date 2016/12/24 18:12
 */
public abstract class AbstractFollowSellExtractor implements FollowSellExtractor {

    List<FollowSell> sFollowSellList;

    @Override
    public List<FollowSell> extract(String asin, Page page) {
        sFollowSellList = new ArrayList<>();

        List<Selectable> followSellNodes = page.getHtml().xpath("//div[@class='a-row a-spacing-mini olpOffer']").nodes();

        FollowSell followSell;
        for (Selectable mItemNode : followSellNodes) {
            followSell = new FollowSell();

            followSell.siteCode = R.SiteCode.US;

            followSell.asin = asin;

            followSell.price = mItemNode.xpath("//span[@class='a-size-large a-color-price olpOfferPrice a-text-bold']/text()").get().trim();

            String policyHtml = mItemNode.xpath("//span[@class='a-color-secondary']/html()").get();
            if (StringUtils.isNotEmpty(policyHtml)) {
                followSell.transPolicy = policyHtml.trim().replaceAll("<.*?>", " ");
            }
            followSell.condition = mItemNode.xpath("//span[@class='a-size-medium olpCondition a-text-bold']/text()").get().trim();

            followSell.sellerID = mItemNode.xpath("//span[@class='a-size-medium a-text-bold']/a/@href").regex("seller=([0-9a-zA-Z]*)").get();
            followSell.sellerID = StringUtils.isEmpty(followSell.sellerID) ? "Amazon" : followSell.sellerID;

            followSell.sellerName = mItemNode.xpath("//span[@class='a-size-medium a-text-bold']/a/text()").get();

            followSell.rating = mItemNode.xpath("//p[@class='a-spacing-small']/text()").regex("(\\(.*\\))").get();
            followSell.rating = "()".equals(followSell.rating) ? null : followSell.rating;

            followSell.probability = mItemNode.xpath("//p[@class='a-spacing-small']/a/b/text()").get();
            followSell.starLevel = mItemNode.xpath("//p[@class='a-spacing-small']/i/span/text()").get();

            sFollowSellList.add(followSell);
        }

        return sFollowSellList;
    }

}
