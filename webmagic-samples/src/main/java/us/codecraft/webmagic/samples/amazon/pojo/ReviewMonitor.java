package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.1
 * Review监听对象
 * 2016/10/11
 */
public class ReviewMonitor extends BasePojo {

    public String smrReviewId;
    public String customerCode;
    public int status; /*对应客户下的review状态*/
    public String siteCode;
    public int smrMarked = 1;
    public int smrParsed;
    public int smrPriority;
    public int frequency; /*执行频率（h/次）*/
    public Date finishTime;

    public ReviewMonitor() {
    }

    public ReviewMonitor(String smrReviewId) {
        this.smrReviewId = smrReviewId;
    }

    @Override
    public String toString() {
        return "ReviewMonitor{" +
                "smrReviewId='" + smrReviewId + '\'' +
                ", siteCode='" + customerCode + '\'' +
                ", siteCode='" + status + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", smrMarked=" + smrMarked +
                ", smrParsed=" + smrParsed +
                ", finishTime=" + frequency +
                ", finishTime=" + finishTime +
                ", smrPriority=" + smrPriority +
                '}';
    }
}
