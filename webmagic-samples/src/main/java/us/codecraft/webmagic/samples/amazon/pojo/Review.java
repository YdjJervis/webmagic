package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/11
 */
public class Review extends BasePojo {

    public static final String PRODUCT_REVIEWS = "product-reviews";

    /**
     * 站点码
     */
    public String basCode;
    /**
     * Asin码
     */
    public String saaAsin;
    /**
     * 根ASIN
     */
    public String saaRootAsin;
    /**
     * 评论者ID
     */
    public String sarPersonId;
    /**
     * 评论时间
     */
    public String sarTime;
    /**
     * 评论时间格式化后的时间
     */
    public Date sarDealTime;
    /**
     * 评论者
     */
    public String sarPerson;
    /**
     * 评论ID
     */
    public String sarReviewId;
    /**
     * 标记是否监控0-no;1-yes
     */
    public int sarMonitor = 0;
    /**
     * 购买状态
     */
    public String sarBuyStatus;
    /**
     * 评论者打分
     */
    public int sarStar;
    /**
     * 购买版本信息
     */
    public String sarVersion;
    /**
     * 评论内容
     */
    public String sarContent;
    /**
     * 评论标题
     */
    public String sarTitle;

    public String sarPageNum;

    @Override
    public String toString() {
        return "Review{" +
                "basCode='" + basCode + '\'' +
                ", saaAsin='" + saaAsin + '\'' +
                ", saaRootAsin='" + saaRootAsin + '\'' +
                ", sarPersonId='" + sarPersonId + '\'' +
                ", sarTime='" + sarTime + '\'' +
                ", sarDealTime=" + sarDealTime +
                ", sarPerson='" + sarPerson + '\'' +
                ", sarReviewId='" + sarReviewId + '\'' +
                ", sarMonitor=" + sarMonitor +
                ", sarBuyStatus='" + sarBuyStatus + '\'' +
                ", sarStar=" + sarStar +
                ", sarVersion='" + sarVersion + '\'' +
                ", sarContent='" + sarContent + '\'' +
                ", sarTitle='" + sarTitle + '\'' +
                ", sarPageNum='" + sarPageNum + '\'' +
                '}';
    }
}
