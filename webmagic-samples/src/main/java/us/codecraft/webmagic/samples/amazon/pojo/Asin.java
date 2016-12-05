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
    public String siteCode;
    public String asin;
    public String rootAsin;
    /**
     * 爬取状态，0未爬取，1已经爬取，2爬取完毕
     */
    public String status = "0-0-0-0-0";
    /**
     * 爬取进度
     */
    public float progress;
    public Date syncTime;
    /**
     * 是否在销售
     */
    public int onSale = 1;
    public int crawledHead;

    @Override
    public String toString() {
        return "Asin{" +
                "siteCode='" + siteCode + '\'' +
                ", asin='" + asin + '\'' +
                ", rootAsin='" + rootAsin + '\'' +
                ", status='" + status + '\'' +
                ", progress=" + progress +
                ", syncTime=" + syncTime +
                ", onSale=" + onSale +
                ", crawledHead=" + crawledHead +
                '}';
    }
}
