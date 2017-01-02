package com.eccang.spider.amazon.pojo.batch;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 批次跟卖实体
 * @date 2016/12/26 17:34
 */
public class BatchFollowSell extends BasePojo {

    public String batchNumber;
    public String siteCode;
    public String asin;
    public int type;
    public int status;
    public int priority;
    public int isChanged;

    @Override
    public String toString() {
        return "BatchFollowSell{" +
                "batchNumber='" + batchNumber + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", asin='" + asin + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", priority=" + priority +
                ", isChanged=" + isChanged +
                '}';
    }
}
