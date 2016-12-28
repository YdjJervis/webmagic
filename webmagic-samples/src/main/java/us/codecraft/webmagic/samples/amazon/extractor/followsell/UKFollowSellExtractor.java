package us.codecraft.webmagic.samples.amazon.extractor.followsell;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.FollowSell;

import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 英国站跟卖信息抽取器
 * @date 2016/12/24 18:14
 */
public class UKFollowSellExtractor extends AbstractFollowSellExtractor {

    @Override
    public List<FollowSell> extract(String asin, Page page) {
        super.extract(asin, page);
        return sFollowSellList;
    }

}
