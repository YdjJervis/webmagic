package com.eccang.pojo;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * 查询关键词排名搜索到的信息
 * 2017/1/4 14:51
 */
public class KeywordRankQueryRsp extends BaseRspParam {

    private List<KeywordRankInfo> data;

    public List<KeywordRankInfo> getData() {
        return data;
    }

    public void setData(List<KeywordRankInfo> data) {
        this.data = data;
    }

    public class KeywordRankInfo {
        private String asin;
        private String keyword;
        private String siteCode;
        private String departmentCode;
        private int totalPages;
        private int everyPage;
        private int rankNum;
        private List<GoodsInfo> top10;

        public List<GoodsInfo> getTop10() {
            return top10;
        }

        public void setTop10(List<GoodsInfo> top10) {
            this.top10 = top10;
        }

        public String getAsin() {
            return asin;
        }

        public void setAsin(String asin) {
            this.asin = asin;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getSiteCode() {
            return siteCode;
        }

        public void setSiteCode(String siteCode) {
            this.siteCode = siteCode;
        }

        public String getDepartmentCode() {
            return departmentCode;
        }

        public void setDepartmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getEveryPage() {
            return everyPage;
        }

        public void setEveryPage(int everyPage) {
            this.everyPage = everyPage;
        }

        public int getRankNum() {
            return rankNum;
        }

        public void setRankNum(int rankNum) {
            this.rankNum = rankNum;
        }

        public class GoodsInfo {
            private int rankNum;
            private String title;
            private String price;
            private String deliveryMode; /*发货方式*/
            private String distributionMode; /*配送方式*/
            private String pictureUrl;
            private String offersNum; /*跟卖数*/
            private String department; /*品类信息*/
            private String goodsStatus;

            public int getRankNum() {
                return rankNum;
            }

            public void setRankNum(int rankNum) {
                this.rankNum = rankNum;
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

            public String getPictureUrl() {
                return pictureUrl;
            }

            public void setPictureUrl(String pictureUrl) {
                this.pictureUrl = pictureUrl;
            }

            public String getOffersNum() {
                return offersNum;
            }

            public void setOffersNum(String offersNum) {
                this.offersNum = offersNum;
            }

            public String getDepartment() {
                return department;
            }

            public void setDepartment(String department) {
                this.department = department;
            }

            public String getGoodsStatus() {
                return goodsStatus;
            }

            public void setGoodsStatus(String goodsStatus) {
                this.goodsStatus = goodsStatus;
            }
        }
    }

}