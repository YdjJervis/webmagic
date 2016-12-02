package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 跟卖监听 实体
 * @date 2016/12/2 11:18
 */
public class FollowSellMonitor extends BasePojo{

    public String siteCode;
    public String asin;
    public int marked = 1;
    public int parsed;
    public int priority;

    public FollowSellMonitor() {
    }

    public FollowSellMonitor(String siteCode, String asin) {
        this.siteCode = siteCode;
        this.asin = asin;
    }

    @Override
    public String toString() {
        return "FollowSell{" +
                "siteCode='" + siteCode + '\'' +
                ", asin='" + asin + '\'' +
                ", marked=" + marked +
                ", parsed=" + parsed +
                ", priority=" + priority +
                '}';
    }
}