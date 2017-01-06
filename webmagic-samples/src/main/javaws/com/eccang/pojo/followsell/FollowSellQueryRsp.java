package com.eccang.pojo.followsell;

import com.eccang.pojo.BaseReqParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 跟卖列表查询 返回参数
 * @date 2017/1/6 10:59
 */
public class FollowSellQueryRsp extends BaseReqParam {

    public List<FollowSell> data = new ArrayList<>();

    public class FollowSell {

        public String batchNum;
        public String siteCode;
        public String asin;
        public String sellerId;
        public String price;
        public String transPolicy;
        public String condition;
        public String rating;
        public String probability;
        public String starLevel;
        public String createTime;
        public String updateTime;


    }
}
