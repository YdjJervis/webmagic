package com.eccang.pojo.followsell;

import com.eccang.pojo.BaseReqParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 添加跟卖监控WS请求对象
 * @date 2017/1/5
 */
public class CusFollowSellAddReq extends BaseReqParam {

    public List<FollowSell> data = new ArrayList<>();

    public class FollowSell {
        public String siteCode;
        public String asin;
        public String sellerId;
        public int frequency;
        public int priority;
    }

}