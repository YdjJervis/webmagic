package com.eccang.pojo.asin;

import com.eccang.pojo.BaseRspParam;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/5 11:58
 */
public class ProductRsp extends BaseRspParam {

    private List<ProductInfo> data;

    public List<ProductInfo> getData() {
        return data;
    }

    public void setData(List<ProductInfo> data) {
        this.data = data;
    }

    public class ProductInfo {
        private String asin;
        private String rootAsin;
        private String siteCode;
        private String sellerId; /*卖家id*/
        private String sellerName; /*卖家名称*/
        private String transId; /*运输方id*/
        private String transName;
        private String title;
        private String price;
        private String imgUrl;
        private String reviewNum; /*评论数量*/
        private String reviewStar;
        private String reviewTime; /*最后一条评论的时间*/
        private String replyNum; /*回复数*/
        private int amazonDelivery; /*是否是亚马逊配送*/
        private String followSellNum;
        private String addedTime; /*上架时间*/
        private String category; /*排行*/
        private String feature; /*产品卖点特征*/

        public String getAsin() {
            return asin;
        }

        public void setAsin(String asin) {
            this.asin = asin;
        }

        public String getRootAsin() {
            return rootAsin;
        }

        public void setRootAsin(String rootAsin) {
            this.rootAsin = rootAsin;
        }

        public String getSiteCode() {
            return siteCode;
        }

        public void setSiteCode(String siteCode) {
            this.siteCode = siteCode;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public String getTransId() {
            return transId;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

        public String getTransName() {
            return transName;
        }

        public void setTransName(String transName) {
            this.transName = transName;
        }

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

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getReviewNum() {
            return reviewNum;
        }

        public void setReviewNum(String reviewNum) {
            this.reviewNum = reviewNum;
        }

        public String getReviewStar() {
            return reviewStar;
        }

        public void setReviewStar(String reviewStar) {
            this.reviewStar = reviewStar;
        }

        public String getReviewTime() {
            return reviewTime;
        }

        public void setReviewTime(String reviewTime) {
            this.reviewTime = reviewTime;
        }

        public String getReplyNum() {
            return replyNum;
        }

        public void setReplyNum(String replyNum) {
            this.replyNum = replyNum;
        }

        public int getAmazonDelivery() {
            return amazonDelivery;
        }

        public void setAmazonDelivery(int amazonDelivery) {
            this.amazonDelivery = amazonDelivery;
        }

        public String getFollowSellNum() {
            return followSellNum;
        }

        public void setFollowSellNum(String followSellNum) {
            this.followSellNum = followSellNum;
        }

        public String getAddedTime() {
            return addedTime;
        }

        public void setAddedTime(String addedTime) {
            this.addedTime = addedTime;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }
    }
}