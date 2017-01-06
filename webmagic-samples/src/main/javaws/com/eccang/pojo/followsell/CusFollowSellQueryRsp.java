package com.eccang.pojo.followsell;

import com.eccang.pojo.BaseRspParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 查询 客户-跟卖 关系返回参数
 * @date 2017/1/6 11:00
 */
public class CusFollowSellQueryRsp extends BaseRspParam {

    public List<CusFollowSell> data = new ArrayList<>();

    public class CusFollowSell {

        public String customerCode;
        public String siteCode;
        public String asin;
        public String sellerId;
        public int crawl;
        public int priority;
        public int frequency;
        public int onSell;
        public String extra;
        public String syncTime;
        public String createTime;
        public String updateTime;

    }
}
