package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: Review监听对象
 * @date 2016/10/11
 */
public class ReviewMonitor extends BasePojo {

    public String smrReviewId;
    public String siteCode;
    public int smrMarked = 1;
    public int smrParsed;
    public int smrPriority;

    public ReviewMonitor() {
    }

    public ReviewMonitor(String smrReviewId) {
        this.smrReviewId = smrReviewId;
    }

    @Override
    public String toString() {
        return "ReviewMonitor{" +
                "smrReviewId='" + smrReviewId + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", smrMarked=" + smrMarked +
                ", smrParsed=" + smrParsed +
                ", smrPriority=" + smrPriority +
                '}';
    }
}
