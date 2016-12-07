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
    public String siteCode;
    /**
     * 根ASIN
     */
    public String rootAsin;
    /**
     * 评论者ID
     */
    public String personId;
    /**
     * 评论时间
     */
    public String time;
    /**
     * 评论时间格式化后的时间
     */
    public Date dealTime;
    /**
     * 评论者
     */
    public String person;
    /**
     * 评论ID
     */
    public String reviewId;
    /**
     * 标记是否监控0-no;1-yes
     */
    public int monitor = 0;
    /**
     * 购买状态
     */
    public String buyStatus;
    /**
     * 评论者打分
     */
    public int star;
    /**
     * 购买版本信息
     */
    public String version;
    /**
     * 评论内容
     */
    public String content;
    /**
     * 评论标题
     */
    public String title;

    public String pageNum;

    /** 二期：优先级 */
    public int priority;

    /** 三期：执行频率 */
    public int frequency; /*执行频率（h/次）*/

    /** 三期：是否监控*/
    public int marked;

    @Override
    public String toString() {
        return "Review{" +
                "basCode='" + siteCode + '\'' +
                ", rootAsin='" + rootAsin + '\'' +
                ", personId='" + personId + '\'' +
                ", time='" + time + '\'' +
                ", dealTime=" + dealTime +
                ", person='" + person + '\'' +
                ", reviewId='" + reviewId + '\'' +
                ", monitor=" + monitor +
                ", buyStatus='" + buyStatus + '\'' +
                ", star=" + star +
                ", version='" + version + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", pageNum='" + pageNum + '\'' +
                ", priority=" + priority +
                ", frequency='" + frequency + '\'' +
                ", marked='" + marked + '\'' +
                '}';
    }
}
