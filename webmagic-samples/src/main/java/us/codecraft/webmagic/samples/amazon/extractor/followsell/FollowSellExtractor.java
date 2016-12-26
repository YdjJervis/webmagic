package us.codecraft.webmagic.samples.amazon.extractor.followsell;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.FollowSell;

import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 跟卖抽取器接口
 * @date 2016/12/24 18:11
 */
public interface FollowSellExtractor {

    List<FollowSell> extract(String asin, Page page);
}
