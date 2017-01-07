package com.eccang.pojo.rank;

import com.eccang.pojo.BaseRspParam;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/2 14:14
 */
public class CusRankRsp extends BaseRspParam {

    public List<CustomerKeywordRank> data;

    public List<CustomerKeywordRank> getData() {
        return data;
    }

    public void setData(List<CustomerKeywordRank> data) {
        this.data = data;
    }

    public class CustomerKeywordRank {
        public String asin;
        public String siteCode;
        public String departmentCode;
        public String keyword;
        public int crawl;
        public int priority;
        public int frequency;
        public String syncTime;
        public String createTime;
        public String updateTime;

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

        public String getSyncTime() {
            return syncTime;
        }

        public void setSyncTime(String syncTime) {
            this.syncTime = syncTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}