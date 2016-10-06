package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

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
    public String saaPriority;
    public String saaSyncTime;

    @Override
    public String toString() {
        return super.toString() + "Asin{" +
                "site=" + site +
                ", asinSource=" + asinSource +
                ", saaAsin='" + saaAsin + '\'' +
                ", saaStar='" + saaStar + '\'' +
                ", saaStatus='" + saaStatus + '\'' +
                ", saaPriority='" + saaPriority + '\'' +
                ", saaSyncTime='" + saaSyncTime + '\'' +
                '}';
    }
}
