package com.eccang.pojo.followsell;

import com.eccang.pojo.BaseReqParam;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 跟卖列表查询 请求参数
 * @date 2017/1/6 10:59
 */
public class FollowSellQueryReq extends BaseReqParam {

    public FollowSell data = new FollowSell();

    public class FollowSell {

        public String batchNum;
        public String siteCode;
        public String asin;

    }
}
