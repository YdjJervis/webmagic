package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * 是否是经验买家
     */
    public String experience;
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
     *星级集合
     */
    public List<Integer> starList = new ArrayList<>();

    /**
     * 购买版本信息
     */
    public String version;
    /**
     * 评论内容
     */
    public String content;
    public String votes;
    /**
     * 评论标题
     */
    public String title;

    public int pageNum;

    public int pageSize;

    /** 二期：优先级 */
    public int priority;

    /** 三期：执行频率 */
    public int frequency; /*执行频率（h/次）*/

    /** 三期：是否监控*/
    public int marked;

    @Override
    public String toString() {
        return "Review{" +
                "siteCode='" + siteCode + '\'' +
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
                ", votes='" + votes + '\'' +
                ", title='" + title + '\'' +
                ", pageNum='" + pageNum + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", priority=" + priority +
                ", frequency=" + frequency +
                ", marked=" + marked +
                '}';
    }
}
