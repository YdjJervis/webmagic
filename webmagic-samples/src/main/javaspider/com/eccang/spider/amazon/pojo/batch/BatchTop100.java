package com.eccang.spider.amazon.pojo.batch;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Hardy
 * @version V0.2
 * 2017/2/9 15:28
 */
public class BatchTop100 extends BasePojo {

    public String batchNum;
    public String siteCode;
    public int type;
    public float progress;
    public int status;

    @Override
    public String toString() {
        return "BatchTop100{" +
                "batchNum='" + batchNum + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", type=" + type +
                ", progress=" + progress +
                ", status=" + status +
                '}';
    }
}