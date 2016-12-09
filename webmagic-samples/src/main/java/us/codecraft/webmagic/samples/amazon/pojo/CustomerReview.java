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

    public String reviewId;
    public String customerCode;
    /**
     * 对应客户下的review状态
     */
    public int status = 2;
    public String siteCode;
    public int marked = 1;
    public int parsed;
    public int priority;
    /**
     * 执行频率（h/次）
     */
    public int frequency;
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
                ", marked=" + marked +
                ", parsed=" + parsed +
                ", priority=" + priority +
                ", frequency=" + frequency +
                ", finishTime=" + finishTime +
                '}';
    }
}
