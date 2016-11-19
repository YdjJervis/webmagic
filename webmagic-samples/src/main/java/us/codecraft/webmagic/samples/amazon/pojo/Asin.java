package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

import java.util.Date;

/**
 * @author Jervis
 * @version V0.1
 * @Description: ASIN码对象
 * @date 2016/10/11
 */
public class Asin extends BasePojo {

    /**
     * asin码
     */
    public Site site = new Site();
    public AsinSource asinSource = new AsinSource();
    public String saaAsin;
    public String saaRootAsin;
    public String saaStar;
    /**
     * 爬取状态，0未爬取，1已经爬取，2爬取完毕
     */
    public String saaStatus = "0-0-0-0-0";
    /**
     * 爬取进度
     */
    public float saaProgress;
    /**
     * 已经转换成URL过
     */
    public int saaParsed;
    public int saaPriority;
    public int saaNeedUpdatting = 1;
    public int saaIsUpdatting;
    public Date saaSyncTime;
    /**
     * 是否在销售
     */
    public int saaOnSale = 1;
    public int saaCrawledHead;

    @Override
    public String toString() {
        return "Asin{" +
                "site=" + site +
                ", asinSource=" + asinSource +
                ", saaAsin='" + saaAsin + '\'' +
                ", saaRootAsin='" + saaRootAsin + '\'' +
                ", saaStar='" + saaStar + '\'' +
                ", saaStatus='" + saaStatus + '\'' +
                ", saaProgress=" + saaProgress +
                ", saaParsed=" + saaParsed +
                ", saaPriority=" + saaPriority +
                ", saaNeedUpdatting=" + saaNeedUpdatting +
                ", saaIsUpdatting=" + saaIsUpdatting +
                ", saaSyncTime=" + saaSyncTime +
                ", saaOnSale=" + saaOnSale +
                ", saaCrawledHead=" + saaCrawledHead +
                '}';
    }
}
