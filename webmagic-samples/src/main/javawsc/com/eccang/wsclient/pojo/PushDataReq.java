package com.eccang.wsclient.pojo;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 *          推送数据对象
 *          2016/12/13 14:41
 */
public class PushDataReq {

    /**
     * customerCode : xxx
     * platformCode : xxx
     * token : xxx
     * data : {"type":"update/add","batchNum":"EC20161208152146151","asins":[{"asin":"B0181YRLT4","rootAsin":"B0181YRLT4","reviews":[{"reviewId":"R2V7LL01LD8CLX","siteCode":"US","star":"5"},{"reviewId":"R2V7LL01LD8CDS","siteCode":"US","star":"5"}]},{"asin":"R2V7LL01LD6DSA","rootAsin":"B0181YRLT4","reviews":[{"reviewId":"R2V7LL01LD6DSA","siteCode":"US","star":"1"},{"reviewId":"R2V7LL01LD6DQA","siteCode":"US","star":"4"}]}]}
     */
    private String wsUrl;
    private String customerCode;
    private String platformCode;
    private String token;
    private Data data;

    public String getWsUrl() {
        return wsUrl;
    }

    public void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        /**
         * type : 0（全量）/1（监听）/2（更新）
         * batchNum : EC20161208152146151
         * asins : [{"asin":"B0181YRLT4","rootAsin":"B0181YRLT4","reviews":[{"reviewId":"R2V7LL01LD8CLX","siteCode":"US","star":"5"},{"reviewId":"R2V7LL01LD8CDS","siteCode":"US","star":"5"}]},{"asin":"R2V7LL01LD6DSA","rootAsin":"B0181YRLT4","reviews":[{"reviewId":"R2V7LL01LD6DSA","siteCode":"US","star":"1"},{"reviewId":"R2V7LL01LD6DQA","siteCode":"US","star":"4"}]}]
         */

        private String type;
        private String batchNum;
        private List<Asin> asins;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBatchNum() {
            return batchNum;
        }

        public void setBatchNum(String batchNum) {
            this.batchNum = batchNum;
        }

        public List<Asin> getAsins() {
            return asins;
        }

        public void setAsins(List<Asin> asins) {
            this.asins = asins;
        }

        public static class Asin {
            /**
             * asin : B0181YRLT4
             * rootAsin : B0181YRLT4
             * reviews : [{"reviewId":"R2V7LL01LD8CLX","siteCode":"US","star":"5"},{"reviewId":"R2V7LL01LD8CDS","siteCode":"US","star":"5"}]
             */

            private String asin;
            private String siteCode;
            private List<Review> reviews;

            public String getAsin() {
                return asin;
            }

            public void setAsin(String asin) {
                this.asin = asin;
            }

            public String getSiteCode() {
                return siteCode;
            }

            public void setSiteCode(String siteCode) {
                this.siteCode = siteCode;
            }

            public List<Review> getReviews() {
                return reviews;
            }

            public void setReviews(List<Review> reviews) {
                this.reviews = reviews;
            }

            public static class Review {
                /**
                 * reviewId : R2V7LL01LD8CLX
                 * siteCode : US
                 * star : 5
                 */

                private String reviewId;
                private String star;

                public String getReviewId() {
                    return reviewId;
                }

                public void setReviewId(String reviewId) {
                    this.reviewId = reviewId;
                }

                public String getStar() {
                    return star;
                }

                public void setStar(String star) {
                    this.star = star;
                }
            }
        }
    }
}
