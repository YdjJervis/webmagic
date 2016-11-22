package com.eccang.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: ASIN批次查询返回结构
 * @date 2016/11/22 14:15
 */
public class BatchAsinRsp extends BaseRspParam {

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
        public List<Asin> details = new ArrayList<Asin>();

    }

    public class Asin {
        public String siteCode;
        public String asin;
        public String rootAsin;
        public int crawled;
        public double progress;
        public Date startTime;
        public Date finishTime;
        public Date updateTime;

    }
}