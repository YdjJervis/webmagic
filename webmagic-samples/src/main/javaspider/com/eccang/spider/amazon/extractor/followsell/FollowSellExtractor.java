package com.eccang.spider.amazon.extractor.followsell;

import us.codecraft.webmagic.Page;
import com.eccang.spider.amazon.pojo.crawl.FollowSell;

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
