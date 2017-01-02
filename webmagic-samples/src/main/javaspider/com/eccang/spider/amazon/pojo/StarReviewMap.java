package com.eccang.spider.amazon.pojo;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 需要爬取的的星级和该星级最后的评论的ID
 * @date 2016/10/14 15:09
 */
public class StarReviewMap {

    /**
     * 需要爬取的星级。取值范围[1,5]
     */
    public int star;
    /**
     * 该星级对应最后评论时间
     */
    public String reviewID;

    public int reviewNum;

    public StarReviewMap(int star, String reviewID) {
        this.star = star;
        this.reviewID = reviewID;
    }

    @Override
    public String toString() {
        return "StarReviewMap{" +
                "star=" + star +
                ", reviewID='" + reviewID + '\'' +
                ", reviewNum=" + reviewNum +
                '}';
    }
}