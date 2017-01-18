package com.eccang.pojo.rank;

import com.eccang.pojo.BaseRspParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/5 17:13
 */
public class BatchRankRsp extends BaseRspParam {
    public Batch data = new Batch();

    public class Batch {
        public String number;
        public String createTime;
        public int type;
        public int status; /*批次爬取状态*/
        public int timesTotal;
        public int timesValid;
        public String startTime;
        public String finishTime;
        public double progress;
        public String updateTime;
        public List<KeywordRank> details = new ArrayList<>();
    }

    public class KeywordRank {
        public String siteCode;
        public String departmentCode;
        public String keyword;
        public String asin;
        public double progress;
        public int isChanged;
        public int priority;
        public String createTime;
        public String updateTime;
    }
}