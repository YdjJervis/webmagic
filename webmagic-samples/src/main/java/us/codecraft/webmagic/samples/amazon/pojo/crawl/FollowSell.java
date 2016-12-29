package us.codecraft.webmagic.samples.amazon.pojo.crawl;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 跟卖 实体
 * @date 2016/12/2 15:18
 */
public class FollowSell extends BasePojo{

    public String siteCode;
    public String asin;
    public String sellerID;
    public String price;
    public String transPolicy = "";
    public String condition;
    public String sellerName;
    public String rating;
    /**
     * 好评率
     */
    public String probability;
    public String starLevel;

    @Override
    public String toString() {
        return "FollowSell{" +
                "siteCode='" + siteCode + '\'' +
                ", asin='" + asin + '\'' +
                ", sellerID='" + sellerID + '\'' +
                ", price='" + price + '\'' +
                ", transPolicy='" + transPolicy + '\'' +
                ", condition='" + condition + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", rating='" + rating + '\'' +
                ", probability='" + probability + '\'' +
                ", starLevel='" + starLevel + '\'' +
                '}';
    }
}