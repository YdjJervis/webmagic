package us.codecraft.webmagic.samples.amazon.util;

import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.samples.amazon.pojo.FollowSell;
import us.codecraft.webmagic.selector.Selectable;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 跟卖条目抽取器
 * @date 2016/12/2 16:47
 */
public class FollowSellExtractor {

    private String mSiteCode;
    private String mAsin;
    private Selectable mItemNode;

    public FollowSellExtractor(String siteCode, String asin, Selectable itemNode) {
        mSiteCode = siteCode;
        mAsin = asin;
        mItemNode = itemNode;
    }

    public FollowSell extract(){
        FollowSell followSell = new FollowSell();
        followSell.siteCode = mSiteCode;
        followSell.asin = mAsin;

        if("US".equals(mSiteCode)){
            followSell.price = mItemNode.xpath("//span[@class='a-size-large a-color-price olpOfferPrice a-text-bold']/text()").get().trim();

            String policyHtml = mItemNode.xpath("//span[@class='a-color-secondary']/html()").get();
            if(StringUtils.isNotEmpty(policyHtml)){
                followSell.transPolicy = policyHtml.trim().replaceAll("<a.*/a>","");
            }
            followSell.condition =mItemNode.xpath("//span[@class='a-size-medium olpCondition a-text-bold']/text()").get().trim();
            followSell.sellerID = mItemNode.xpath("//span[@class='a-size-medium a-text-bold']/a/@href").regex("seller=([0-9a-zA-Z]*)").get();
            followSell.sellerName = mItemNode.xpath("//span[@class='a-size-medium a-text-bold']/a/text()").get();
            followSell.rating = mItemNode.xpath("//p[@class='a-spacing-small']/text()").regex("(\\(.*\\))").get();
            followSell.probability = mItemNode.xpath("//p[@class='a-spacing-small']/a/b/text()").get();
            followSell.starLevel = mItemNode.xpath("//p[@class='a-spacing-small']/i/span/text()").get();
            return followSell;
        }

        return followSell;
    }
}