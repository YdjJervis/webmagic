package com.eccang.pojo.followsell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description: 设置 客户-跟卖 关系WS请求对象
 * @date 2017/1/2 15:58
 */
public class CusFollowSellUpdateReq extends CusFollowSellAddReq {

    public List<FollowSell> data = new ArrayList<>();

    public class FollowSell extends CusFollowSellAddReq.FollowSell {
        public int crawl;
    }
}