package us.codecraft.webmagic.samples.amazon.pojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.1
 *  通过关键词搜索的商品排名信息
 *  2016/11/30 15:21
 */
public class GoodsRankInfo {
    private String title;
    private String price;
    private String deliveryMode;/*发货方式*/
    private String distributionMode;/*配送方式*/
    private String goodsPictureUrl;/*商品图片URL*/
    private int offersNum;/*跟卖数*/
    private int rankNum;/*排名*/
    private String departmentInfo; /*品类信息*/
    private String goodsStatus; /*排行商品状态(Bestseller or Gesponsert)*/
    private Date createDate;
    private Date updateDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getDistributionMode() {
        return distributionMode;
    }

    public void setDistributionMode(String distributionMode) {
        this.distributionMode = distributionMode;
    }

    public String getGoodsPictureUrl() {
        return goodsPictureUrl;
    }

    public void setGoodsPictureUrl(String goodsPictureUrl) {
        this.goodsPictureUrl = goodsPictureUrl;
    }

    public int getOffersNum() {
        return offersNum;
    }

    public void setOffersNum(int offersNum) {
        this.offersNum = offersNum;
    }

    public int getRankNum() {
        return rankNum;
    }

    public void setRankNum(int rankNum) {
        this.rankNum = rankNum;
    }

    public String getDepartmentInfo() {
        return departmentInfo;
    }

    public void setDepartmentInfo(String departmentInfo) {
        this.departmentInfo = departmentInfo;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}