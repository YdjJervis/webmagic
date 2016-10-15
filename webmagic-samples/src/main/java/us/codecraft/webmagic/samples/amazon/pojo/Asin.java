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
    public Site site;
    public AsinSource asinSource;
    public String saaAsin;
    public String saaStar;
    /**
     * 爬取状态，0未爬取，1已经爬取，2爬取完毕
     */
    public String saaStatus;
    /**
     * 爬取进度
     */
    public float saaProgress;
    /**
     * 已经转换成URL过
     */
    public int saaParsed;
    public int saaPriority;
    public int saaIsUpdatting;
    public Date saaSyncTime;

    @Override
    public String toString() {
        return super.toString() + "Asin{" +
                "site=" + site +
                ", asinSource=" + asinSource +
                ", saaAsin='" + saaAsin + '\'' +
                ", saaStar='" + saaStar + '\'' +
                ", saaStatus='" + saaStatus + '\'' +
                ", saaProgress='" + saaProgress + '\'' +
                ", saaParsed='" + saaParsed + '\'' +
                ", saaPriority='" + saaPriority + '\'' +
                ", saaIsUpdatting='" + saaIsUpdatting + '\'' +
                ", saaSyncTime='" + saaSyncTime + '\'' +
                '}';
    }
}
