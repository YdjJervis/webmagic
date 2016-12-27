package us.codecraft.webmagic.samples.amazon.extractor.followsell;

import org.apache.log4j.Logger;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.pojo.crawl.FollowSell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 跟卖抽取器抽象类，做一些公共操作
 * @date 2016/12/24 18:12
 */
public abstract class AbstractFollowSellExtractor implements FollowSellExtractor{

    Logger sLogger = Logger.getLogger(getClass());
    List<FollowSell> sFollowSellList;

    @Override
    public List<FollowSell> extract(String asin, Page page) {
        sFollowSellList = new ArrayList<>();
        return sFollowSellList;
    }
}
