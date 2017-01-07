package com.eccang.pojo.rank;

import com.eccang.pojo.BaseRspParam;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * 查询关键词排名搜索到的信息
 * 2017/1/4 14:51
 */
public class RankQueryRsp extends BaseRspParam {

    public KeywordRankInfo data;

    public KeywordRankInfo getData() {
        return data;
    }

    public void setData(KeywordRankInfo data) {
        this.data = data;
    }

    public class KeywordRankInfo {
        public String asin;
        public String keyword;
        public String siteCode;
        public String departmentCode;
        public int totalPages;
        public int everyPage;
        public int rankNum;
        public String batchNum;
        public List<GoodsInfo> top10;

        public String getBatchNum() {
            return batchNum;
        }

        public void setBatchNum(String batchNum) {
            this.batchNum = batchNum;
        }

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
            public int rankNum;
            public String title;
            public String price;
            public String deliveryMode; /*发货方式*/
            public String distributionMode; /*配送方式*/
            public String pictureUrl;
            public int offersNum; /*跟卖数*/
            public String department; /*品类信息*/
            public String goodsStatus;

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

            public int getOffersNum() {
                return offersNum;
            }

            public void setOffersNum(int offersNum) {
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