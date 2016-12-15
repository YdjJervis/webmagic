package com.eccang.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: ASIN批次查询返回结构
 * @date 2016/11/22 14:15
 */
public class BatchAsinRsp extends BaseRspParam {

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
        public List<Asin> details = new ArrayList<Asin>();

    }

    public class Asin {
        public String siteCode;
        public String asin;
        public String rootAsin;
        public int crawled;
        public double progress;
        public String startTime;
        public String finishTime;
        public String updateTime;

    }
}