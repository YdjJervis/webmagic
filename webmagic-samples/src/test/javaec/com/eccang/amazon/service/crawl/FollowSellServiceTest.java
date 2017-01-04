package com.eccang.amazon.service.crawl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.amazon.pojo.crawl.FollowSell;
import com.eccang.spider.amazon.service.crawl.FollowSellService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/1/2 14:19
 */
public class FollowSellServiceTest extends SpringTestCase {

    @Autowired
    private FollowSellService mService;

    @Test
    public void findAll() {
        FollowSell followSell = new FollowSell();
        followSell.batchNum = "Batch001";
        System.out.println(mService.findAll(followSell));
    }

    @Test
    public void addAll() {
        List<FollowSell> list = new ArrayList<>();

        FollowSell sell = getFollowSell();
        sell.condition = "Used - Good";
        sell.price = "$24.25";
        list.add(sell);

        sell = getFollowSell();
        sell.condition = "New";
        sell.price = "$30.17";
        list.add(sell);

        mService.addAll(list);
    }

    private FollowSell getFollowSell() {
        FollowSell followSell = new FollowSell();
        followSell.batchNum = "EC20170104134000146";
        followSell.siteCode = "US";
        followSell.asin = "0071809252";
        followSell.sellerID = "A1X11F1UOSYIK1";
        return followSell;
    }
}
