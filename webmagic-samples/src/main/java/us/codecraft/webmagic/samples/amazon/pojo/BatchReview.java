package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次产生的Review监控进度单
 * @date 2016/11/10 14:08
 */
public class BatchReview extends BasePojo {

    public String batchNumber;
    public String siteCode;
    public String reviewID;
    public int type;
    public int status;
    public int priority;

    @Override
    public String toString() {
        return "BatchReview{" +
                "batchNumber='" + batchNumber + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", reviewID='" + reviewID + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}