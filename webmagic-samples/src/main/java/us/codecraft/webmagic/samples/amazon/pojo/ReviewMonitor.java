package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * Review监听表
 */
public class ReviewMonitor extends BasePojo {

    public String smrReviewId;
    public int smrMarked;

    @Override
    public String toString() {
        return super.toString() + "ReviewMonitor{" +
                "slrReviewId='" + smrReviewId + '\'' +
                ", slrMarked=" + smrMarked +
                '}';
    }
}
