package com.eccang.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: Review监控批次查询返回结构
 * @date 2016/11/22 14:15
 */
public class BatchReviewRsp extends BaseRspParam {

    public Batch data;

    public class Batch {

        public String number;
        public Date createTime;
        public int type;
        public int times;
        public Date startTime;
        public Date finishTime;
        public double progress;
        public Date updateTime;
        public List<ReviewMonitor> details = new ArrayList<ReviewMonitor>();
    }

    public class ReviewMonitor {
        public String siteCode;
        public String reviewID;
        public int times;
        public Date updateTime;
    }
}