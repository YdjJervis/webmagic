package com.eccang.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: Review监控批次查询返回结构
 * @date 2016/11/22 14:15
 */
public class BatchReviewRsp extends BaseRspParam {

    public Batch data = new Batch();

    public class Batch {

        public String number;
        public String createTime;
        public int type;
        public int times;
        public String startTime;
        public String finishTime;
        public double progress;
        public String updateTime;
        public List<ReviewMonitor> details = new ArrayList<ReviewMonitor>();
    }

    public class ReviewMonitor {
        public String siteCode;
        public String asin;
        public int crawled;
        public String reviewID;
        public int times;
        public String updateTime;
    }
}