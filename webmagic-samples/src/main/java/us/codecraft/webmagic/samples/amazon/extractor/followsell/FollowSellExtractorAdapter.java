package us.codecraft.webmagic.samples.amazon.extractor.followsell;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.R;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.FollowSell;

import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 跟卖抽取站点适配器
 * @date 2016/12/26 9:15
 */
public class FollowSellExtractorAdapter {

    public List<FollowSell> extract(String siteCode, String asin, Page page) {

        FollowSellExtractor extractor = null;
        if (siteCode.equals(R.SiteCode.US)) {
            extractor = new USFollowSellExtractor();
        }

        if (extractor != null) {
            return extractor.extract(asin, page);
        } else {
            return null;
        }
    }
}
