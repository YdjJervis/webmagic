package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次产生的Review监控进度单
 * @date 2016/11/10 14:08
 */
public class BatchReview extends BasePojo {

    public String siteCode;
    public String batchNumber;
    public String reviewID;
    public int times;
    public int crawled;

    @Override
    public String toString() {
        return "BatchReview{" +
                "siteCode='" + siteCode + '\'' +
                ", batchNumber='" + batchNumber + '\'' +
                ", reviewID='" + reviewID + '\'' +
                ", times=" + times +
                '}';
    }
}