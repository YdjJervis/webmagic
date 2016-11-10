package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次单号
 * @date 2016/11/10 14:02
 */
public class Batch extends BasePojo {

    public String number;
    public String customerCode;
    public int type;
    public int status;
    public Date startTime;
    public Date finishTime;
    public float progress;

    @Override
    public String toString() {
        return "Batch{" +
                "number='" + number + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", progress=" + progress +
                '}';
    }
}