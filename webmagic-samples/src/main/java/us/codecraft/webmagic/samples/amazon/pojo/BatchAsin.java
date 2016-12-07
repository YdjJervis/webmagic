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

    public String batchNumber;
    public String siteCode;
    public String asin;
    public String rootAsin;
    public String star = "0-0-1-1-1";
    public int crawled;
    /**
     * 0-未爬取/1-爬取中/2-首页爬取完毕/3-评论爬取完毕
     */
    public int status;
    public float progress;
    public int type;
    public Date startTime;
    public Date finishTime;

    @Override
    public String toString() {
        return "BatchAsin{" +
                "batchNumber='" + batchNumber + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", asin='" + asin + '\'' +
                ", rootAsin='" + rootAsin + '\'' +
                ", star='" + star + '\'' +
                ", crawled=" + crawled +
                ", status=" + status +
                ", progress=" + progress +
                ", type=" + type +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                '}';
    }
}