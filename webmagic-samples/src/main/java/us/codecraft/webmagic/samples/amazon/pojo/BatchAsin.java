package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次产生的ASIN爬取进度单
 * @date 2016/11/10 14:11
 */
public class BatchAsin extends BasePojo {

    public String siteCode;
    public String batchNumber;
    public String asin;
    public String rootAsin;
    public int crawled;
    public float progress;
    public int type;
    public Date startTime;
    public Date finishTime;

    @Override
    public String toString() {
        return "BatchAsin{" +
                "siteCode='" + siteCode + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", asin='" + asin + '\'' +
                ", rootAsin='" + rootAsin + '\'' +
                ", crawled=" + crawled +
                ", progress=" + progress +
                ", type=" + type +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                '}';
    }
}