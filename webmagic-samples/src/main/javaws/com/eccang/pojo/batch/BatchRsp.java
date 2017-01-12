package com.eccang.pojo.batch;

import com.eccang.pojo.BaseRspParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description: 查询总单信息响应
 * @date 2017/1/12 10:38
 */
public class BatchRsp extends BaseRspParam {

    public List<BatchInfo> data = new ArrayList<>();

    public class BatchInfo {
        public String number;
        public int importType; //导入类型
        public int type;  //爬取类型
        public int status; //爬取状态
        public int times; //请求url次数
        public String startTime;
        public float progress; //批次进度
        public String finishTime;
        public String createTime;
        public String updateTime;
    }
}