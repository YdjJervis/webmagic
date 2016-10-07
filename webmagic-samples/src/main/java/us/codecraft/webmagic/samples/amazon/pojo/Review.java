package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

public class Review extends BasePojo {

    public String basCode;
    public String saaAsin;
    public String sarPersonId;
    public String sarTime;
    public String sarDealTime;
    public String sarPerson;
    public String sarReviewId;
    public String sarBuyStatus;
    public String sarStar;
    public String sarVersion;
    public String sarContent;

    @Override
    public String toString() {
        return super.toString() + "Review{" +
                "basCode='" + basCode + '\'' +
                ", saaAsin='" + saaAsin + '\'' +
                ", sarPersonId='" + sarPersonId + '\'' +
                ", sarTime='" + sarTime + '\'' +
                ", sarDealTime='" + sarDealTime + '\'' +
                ", sarPerson='" + sarPerson + '\'' +
                ", sarReviewId='" + sarReviewId + '\'' +
                ", sarBuyStatus='" + sarBuyStatus + '\'' +
                ", sarStar='" + sarStar + '\'' +
                ", sarVersion='" + sarVersion + '\'' +
                ", sarContent='" + sarContent + '\'' +
                '}';
    }
}
