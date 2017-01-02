package com.eccang.spider.amazon.extractor.followsell;

import us.codecraft.webmagic.Page;
import com.eccang.spider.amazon.pojo.crawl.FollowSell;

import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 美国站跟卖信息抽取器
 * @date 2016/12/24 18:14
 */
public class USFollowSellExtractor extends AbstractFollowSellExtractor {

    @Override
    public List<FollowSell> extract(String asin, Page page) {
        super.extract(asin, page);
        return sFollowSellList;
    }

}
