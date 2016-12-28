package us.codecraft.webmagic.samples.amazon.pojo;

import us.codecraft.webmagic.samples.base.pojo.BasePojo;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 产品 详细信息
 * @date 2016/11/29
 */
public class Product extends BasePojo {

    public String siteCode;
    public String rootAsin;
    public int modelType;
    public String sellerID;
    public String sellerName;
    public String transID;
    public String transName;
    public String title;
    public String price;
    public String imgUrl;
    public String reviewNum;
    public String reviewStar;
    public String reviewTime;
    public String replyNum;
    public String transMode;
    public String sellerNum;
    public String addedTime;
    public String category;

    @Override
    public String toString() {
        return super.toString() + "Product{" +
                "siteCode='" + siteCode + '\'' +
                ", rootAsin='" + rootAsin + '\'' +
                ", modelType=" + modelType +
                ", sellerID='" + sellerID + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", transID='" + transID + '\'' +
                ", transName='" + transName + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", reviewNum='" + reviewNum + '\'' +
                ", reviewStar='" + reviewStar + '\'' +
                ", reviewTime='" + reviewTime + '\'' +
                ", replyNum='" + replyNum + '\'' +
                ", transMode='" + transMode + '\'' +
                ", sellerNum='" + sellerNum + '\'' +
                ", addedTime='" + addedTime + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
