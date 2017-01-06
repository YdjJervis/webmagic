package com.eccang.pojo.rank;

import com.eccang.pojo.BaseRspParam;

import java.util.Date;
import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/2 14:14
 */
public class CusRankRsp extends BaseRspParam {

    private List<CustomerKeywordRank> data;

    public List<CustomerKeywordRank> getData() {
        return data;
    }

    public void setData(List<CustomerKeywordRank> data) {
        this.data = data;
    }

    public class CustomerKeywordRank {
        private String asin;
        private String siteCode;
        private String departmentCode;
        private String keyword;
        private int crawl;
        private int priority;
        private int frequency;
        private Date syncTime;
        private Date createTime;
        private Date updateTime;

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

        public String getDepartmentCode() {
            return departmentCode;
        }

        public void setDepartmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public int getCrawl() {
            return crawl;
        }

        public void setCrawl(int crawl) {
            this.crawl = crawl;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public Date getSyncTime() {
            return syncTime;
        }

        public void setSyncTime(Date syncTime) {
            this.syncTime = syncTime;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }
    }
}