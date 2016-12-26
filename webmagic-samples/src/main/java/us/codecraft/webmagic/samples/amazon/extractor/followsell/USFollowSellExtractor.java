package us.codecraft.webmagic.samples.amazon.extractor.followsell;

import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.FollowSell;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 美国站产品基本信息抽取器
 * @date 2016/12/24 18:14
 */
public class USFollowSellExtractor extends AbstractFollowSellExtractor {

    @Override
    public List<FollowSell> extract(String asin, Page page) {
        super.extract(asin, page);

        List<Selectable> followSellNodes = page.getHtml().xpath("//div[@class='a-row a-spacing-mini olpOffer']").nodes();

        FollowSell followSell;
        for (Selectable mItemNode : followSellNodes) {
            followSell = new FollowSell();

            followSell.siteCode = R.SiteCode.US;

            followSell.asin = asin;

            followSell.price = mItemNode.xpath("//span[@class='a-size-large a-color-price olpOfferPrice a-text-bold']/text()").get().trim();

            String policyHtml = mItemNode.xpath("//span[@class='a-color-secondary']/html()").get();
            if (StringUtils.isNotEmpty(policyHtml)) {
                followSell.transPolicy = policyHtml.trim().replaceAll("<a.*/a>", "");
            }
            followSell.condition = mItemNode.xpath("//span[@class='a-size-medium olpCondition a-text-bold']/text()").get().trim();
            followSell.sellerID = mItemNode.xpath("//span[@class='a-size-medium a-text-bold']/a/@href").regex("seller=([0-9a-zA-Z]*)").get();
            followSell.sellerName = mItemNode.xpath("//span[@class='a-size-medium a-text-bold']/a/text()").get();
            followSell.rating = mItemNode.xpath("//p[@class='a-spacing-small']/text()").regex("(\\(.*\\))").get();
            followSell.probability = mItemNode.xpath("//p[@class='a-spacing-small']/a/b/text()").get();
            followSell.starLevel = mItemNode.xpath("//p[@class='a-spacing-small']/i/span/text()").get();

            sFollowSellList.add(followSell);
        }

        return sFollowSellList;
    }

}
