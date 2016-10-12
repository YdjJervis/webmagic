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
    public int smrMarked = 1;
    public int smrParsed;

    public ReviewMonitor(){}

    public ReviewMonitor(String smrReviewId) {
        this.smrReviewId = smrReviewId;
    }

    @Override
    public String toString() {
        return super.toString() + "ReviewMonitor{" +
                "slrReviewId='" + smrReviewId + '\'' +
                ", slrMarked=" + smrMarked +
                ", smrParsed=" + smrParsed +
                '}';
    }
}
