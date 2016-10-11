package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

import java.util.Date;

/**
 * Asin
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
     * 已经转换成URL过
     */
    public int saaParsed;
    public int saaPriority;
    public Date saaSyncTime;

    @Override
    public String toString() {
        return super.toString() + "Asin{" +
                "site=" + site +
                ", asinSource=" + asinSource +
                ", saaAsin='" + saaAsin + '\'' +
                ", saaStar='" + saaStar + '\'' +
                ", saaStatus='" + saaStatus + '\'' +
                ", saaParsed='" + saaParsed + '\'' +
                ", saaPriority='" + saaPriority + '\'' +
                ", saaSyncTime='" + saaSyncTime + '\'' +
                '}';
    }
}
