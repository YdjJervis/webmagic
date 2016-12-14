package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.1
 * @Description Review监听对象
 * @date 2016/10/11
 */
public class CustomerReview extends BasePojo {

    public String customerCode;
    public String siteCode;
    public String asin;
    public String reviewId;
    /**
     * 是否是开启状态。1-是/0-否
     */
    public int status = 2;
    public int priority;
    /**
     * 执行频率（h/次）
     */
    public int frequency;
    public int times;
    public Date finishTime;

    public CustomerReview() {
    }

    public CustomerReview(String reviewID) {
        this.reviewId = reviewID;
    }

    @Override
    public String toString() {
        return "CustomerReview{" +
                "reviewId='" + reviewId + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", status=" + status +
                ", siteCode='" + siteCode + '\'' +
                ", priority=" + priority +
                ", frequency=" + frequency +
                ", times=" + times +
                ", finishTime=" + finishTime +
                '}';
    }
}
