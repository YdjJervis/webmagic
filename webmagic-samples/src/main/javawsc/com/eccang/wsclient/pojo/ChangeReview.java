package com.eccang.wsclient.pojo;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2016/12/14 9:06
 */
public class ChangeReview {

    /**
     * reviewId : R2V7LL01LD8CLX
     * siteCode : US
     * star : 5
     */

    private String reviewId;
    private String siteCode;
    private String star;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }
}