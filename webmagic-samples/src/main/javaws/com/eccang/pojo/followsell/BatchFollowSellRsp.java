package com.eccang.pojo.followsell;

import com.eccang.pojo.BaseRspParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/5 18:09
 */
public class BatchFollowSellRsp extends BaseRspParam {

    public Batch data = new Batch();

    public class Batch {
        public String number;
        public String createTime;
        public int type;
        public int status; /*批次爬取状态*/
        public int times;
        public String startTime;
        public String finishTime;
        public double progress;
        public String updateTime;
        public List<FollowSell> details = new ArrayList<>();
    }

    public class FollowSell {
        public String siteCode;
        public String asin;
        public double progress;
        public int isChanged;
        public int priority;
        public Date createTime;
        public Date updateTime;
    }
}