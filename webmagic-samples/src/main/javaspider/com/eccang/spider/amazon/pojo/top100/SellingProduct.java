package com.eccang.spider.amazon.pojo.top100;

import com.eccang.spider.base.pojo.BasePojo;

/**
 * @author Hardy
 * @version V0.2
 * 热销产品信息
 * 2017/1/19 14:40
 */
public class SellingProduct extends BasePojo {

    public String batchNum;
    public String url;
    public String urlMD5;
    public String siteCode;
    public String depName; /*品类名称*/
    public String depUrl; /*品类url*/
    public String classify; /*商品分类*/
    public String asin;
    public int rankNum;
    public String name; /*商品名*/
    public String imgUrl; /*商品图片URL*/
    public String reviewStar;
    public int reviewNum;
    public String price;
    public int amazonDelivery;
    public int stock; /**/

    @Override
    public String toString() {
        return "SellingProduct{" +
                "batchNum='" + batchNum + '\'' +
                ", url='" + url + '\'' +
                ", urlMD5='" + urlMD5 + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", depName='" + depName + '\'' +
                ", depUrl='" + depUrl + '\'' +
                ", classify='" + classify + '\'' +
                ", asin='" + asin + '\'' +
                ", rankNum=" + rankNum +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", reviewStar='" + reviewStar + '\'' +
                ", reviewNum=" + reviewNum +
                ", price='" + price + '\'' +
                ", amazonDelivery=" + amazonDelivery +
                ", stock=" + stock +
                '}';
    }
}