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
    public String price;
    public String transPolicy = "";
    public String condition;
    public String sellerID;
    public String sellerName;
    public String rating;
    /**
     * 好评率
     */
    public String probability;
    public String starLevel;

    /**
     * 是否是自己店铺产品。1-Y/0-N
     */
    public int self;

    @Override
    public String toString() {
        return "FollowSell{" +
                "siteCode='" + siteCode + '\'' +
                ", asin='" + asin + '\'' +
                ", price='" + price + '\'' +
                ", transPolicy='" + transPolicy + '\'' +
                ", condition='" + condition + '\'' +
                ", sellerID='" + sellerID + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", rating='" + rating + '\'' +
                ", probability='" + probability + '\'' +
                ", starLevel='" + starLevel + '\'' +
                ", self=" + self +
                '}';
    }
}