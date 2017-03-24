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
    public String depCode;
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
    public int stock; /*库存*/
    public int status; /*抓取库存状态（0-未开始；1-转换成url；2-加入购物车；3-统计库存；4-已完成）*/

    @Override
    public String toString() {
        return "SellingProduct{" +
                "batchNum='" + batchNum + '\'' +
                ", url='" + url + '\'' +
                ", urlMD5='" + urlMD5 + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", depCode='" + depCode + '\'' +
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
                ", status=" + status +
                '}';
    }
}